package com.example.coursesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.databinding.CourseItemBinding;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Course> courseList;


    //استدعاء الinterface
    private ClickHandle clickHandle;

    CourseItemBinding binding;


    public CourseAdapter(Context context, List<Course> courseList,ClickHandle clickHandle) {
        this.context = context;
        this.courseList = courseList;
        this.clickHandle=clickHandle;
    }


    //الدالتين لاعطاء الاوامر للعناصر
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=CourseItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MyviewHolder(binding);
    }

    //الوصول لكل عنصر محدد من الليست
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyviewHolder myviewHolder= (MyviewHolder) holder;

        myviewHolder.binding.tvNamecourseItem.setText(courseList.get(position).getTitle());
        myviewHolder.binding.tvDescriptionItem.setText(courseList.get(position).getDescription());
        myviewHolder.binding.tvInstructornameItem.setText(courseList.get(position).getInstructorName());


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
        return courseList.size();
    }


    //ربط التصميم
    public class MyviewHolder extends RecyclerView.ViewHolder{

        CourseItemBinding binding;
        public MyviewHolder(CourseItemBinding binding) {
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
