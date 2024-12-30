package com.example.coursesapp;

import static android.content.Context.MODE_PRIVATE;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.databinding.LectursItemBinding;

import java.util.ArrayList;
import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Lecture> lectureList;


    //استدعاء الinterface
    private ClickHandle clickHandle;

    LectursItemBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public LectureAdapter(Context context, List<Lecture> lectureList,ClickHandle clickHandle) {
        this.context = context;
        this.lectureList = lectureList;
        this.clickHandle=clickHandle;
    }

    public void updateLectures(List<Lecture> newLectures) {
        this.lectureList = newLectures;
        notifyDataSetChanged(); // تحديث البيانات وإعادة العرض
    }
    //الدالتين لاعطاء الاوامر للعناصر
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=LectursItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MyviewHolder(binding);
    }

    //الوصول لكل عنصر محدد من الليست
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        preferences = context.getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();
        long savedid = preferences.getLong("savedid", 0);

        MyviewHolder myviewHolder = (MyviewHolder) holder;
        Appdatabase db = Appdatabase.getDatabase(context.getApplicationContext());
        myviewHolder.binding.tvLecnameItem.setText(lectureList.get(position).getLectureName());
        myviewHolder.binding.tvLecdescritionItem.setText(lectureList.get(position).getDescription());

        List<Long> completedLectures = db.myCoursesDao()
                .getMyCourseByCourseIDAndUserID(savedid, lectureList.get(position).getCourseID())
                .getCompletedLectures();

        // تحقق مما إذا كانت المحاضرة الحالية مكتملة
        if (completedLectures != null && completedLectures.contains(lectureList.get(position).getId())) {
            // إذا كانت المحاضرة مكتملة
            myviewHolder.binding.imageLectureItem.setImageResource(R.drawable.check_circle_24dp_e8eaed_fill1_wght400_grad0_opsz24);
        } else {
            // إذا لم تكن المحاضرة مكتملة
            myviewHolder.binding.imageLectureItem.setImageResource(R.drawable.baseline_play_circle_outline_24);
        }







      //  myviewHolder.binding.lecNameCourseItem.setText(db.courseDao().getCoursesByID(lectureList.get(position).getCourseID()).getTitle());
      //  myviewHolder.binding.gameImag.setImageResource(gamelist.get(position).getImage());


        myviewHolder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickHandle.onItemClick(position);

            }
        });
        myviewHolder.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
             clickHandle.onLongItemClick(position);
                return false;
            }
        });




    }

    @Override
    public int getItemCount() {
        return lectureList.size();
    }


    //ربط التصميم
    public static class MyviewHolder extends RecyclerView.ViewHolder{

        LectursItemBinding binding;
        public MyviewHolder(LectursItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }



    //اصدار امر عند الضغط على عنصر ولكن من main
    public interface ClickHandle{
        void onItemClick(int position);
        void onLongItemClick(int position);


    }



}
