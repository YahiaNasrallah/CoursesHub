package com.example.coursesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.databinding.LectursItemBinding;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Lecture> lectureList;


    //استدعاء الinterface
    private ClickHandle clickHandle;

    LectursItemBinding binding;


    public LectureAdapter(Context context, List<Lecture> lectureList,ClickHandle clickHandle) {
        this.context = context;
        this.lectureList = lectureList;
        this.clickHandle=clickHandle;
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

        MyviewHolder myviewHolder= (MyviewHolder) holder;
        Appdatabase db=Appdatabase.getDatabase(context.getApplicationContext());
        myviewHolder.binding.tvDescrptionItem.setText(lectureList.get(position).getDescription());
        myviewHolder.binding.lecNameItem.setText(lectureList.get(position).getLectureName());
        myviewHolder.binding.lecNumItem.setText(String.valueOf(lectureList.get(position).getLectureNumber()));
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
