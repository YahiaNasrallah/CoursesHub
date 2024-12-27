package com.example.coursesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.databinding.MycoursesItemBinding;
import com.example.coursesapp.databinding.SearchitemBinding;

import java.util.List;

public class searchadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<coursecut> coursecutList;


    //استدعاء الinterface
    private searchadapter.ClickHandle clickHandle;

    SearchitemBinding binding;


    public searchadapter(Context context, List<coursecut> coursecutList, searchadapter.ClickHandle clickHandle) {
        this.context = context;
        this.coursecutList = coursecutList;
        this.clickHandle=clickHandle;
    }


    //الدالتين لاعطاء الاوامر للعناصر
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=SearchitemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new searchadapter.MyviewHolder(binding);
    }

    //الوصول لكل عنصر محدد من الليست
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        searchadapter.MyviewHolder myviewHolder= (searchadapter.MyviewHolder) holder;

        myviewHolder.binding.tvNameCourse.setText(coursecutList.get(position).getTitle());



        myviewHolder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandle.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return coursecutList.size();
    }


    //ربط التصميم
    public class MyviewHolder extends RecyclerView.ViewHolder{

        SearchitemBinding binding;
        public MyviewHolder(SearchitemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }



    //اصدار امر عند الضغط على عنصر ولكن من main
    public interface ClickHandle{
        void onItemClick(int position);

    }



}
