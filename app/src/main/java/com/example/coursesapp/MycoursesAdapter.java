package com.example.coursesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.databinding.CategoryViewItemBinding;
import com.example.coursesapp.databinding.MycoursesItemBinding;

import java.util.List;

public class MycoursesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MyCourses> myCoursesList;


    //استدعاء الinterface
    private MycoursesAdapter.ClickHandle clickHandle;

    MycoursesItemBinding binding;


    public MycoursesAdapter(Context context, List<MyCourses> myCoursesList, MycoursesAdapter.ClickHandle clickHandle) {
        this.context = context;
        this.myCoursesList = myCoursesList;
        this.clickHandle=clickHandle;
    }


    //الدالتين لاعطاء الاوامر للعناصر
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=MycoursesItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MycoursesAdapter.MyviewHolder(binding);
    }

    //الوصول لكل عنصر محدد من الليست
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Appdatabase db=Appdatabase.getDatabase(context);
        MycoursesAdapter.MyviewHolder myviewHolder= (MycoursesAdapter.MyviewHolder) holder;

        myviewHolder.binding.tvCoursename.setText(db.courseDao().getCoursesByID(myCoursesList.get(position).getCourseID()).getTitle());
        myviewHolder.binding.tvHourse.setText("Hourse: "+db.courseDao().getCoursesByID(myCoursesList.get(position).getCourseID()).getHours());
        myviewHolder.binding.tvLecnum.setText("Lecture Numbers: "+db.courseDao().getCoursesByID(myCoursesList.get(position).getCourseID()).getLectureNumber());
        myviewHolder.binding.progressBar.setProgress(myCoursesList.get(position).getProgress());



        myviewHolder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandle.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myCoursesList.size();
    }


    //ربط التصميم
    public class MyviewHolder extends RecyclerView.ViewHolder{

        MycoursesItemBinding binding;
        public MyviewHolder(MycoursesItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }



    //اصدار امر عند الضغط على عنصر ولكن من main
    public interface ClickHandle{
        void onItemClick(int position);

    }



}
