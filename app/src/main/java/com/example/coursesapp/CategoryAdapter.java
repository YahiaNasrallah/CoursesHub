package com.example.coursesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.databinding.CategoryViewItemBinding;
import com.example.coursesapp.databinding.CourseItemBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Category> categoryList;


    //استدعاء الinterface
    private CourseAdapter.ClickHandle clickHandle;

    CategoryViewItemBinding binding;


    public CategoryAdapter(Context context, List<Category> categoryList, CourseAdapter.ClickHandle clickHandle) {
        this.context = context;
        this.categoryList = categoryList;
        this.clickHandle=clickHandle;
    }


    //الدالتين لاعطاء الاوامر للعناصر
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=CategoryViewItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new CategoryAdapter.MyviewHolder(binding);
    }

    //الوصول لكل عنصر محدد من الليست
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        CategoryAdapter.MyviewHolder myviewHolder= (CategoryAdapter.MyviewHolder) holder;

        myviewHolder.binding.tvCatItem.setText(categoryList.get(position).getCategoryName());
        myviewHolder.binding.tvId.setText(String.valueOf(categoryList.get(position).getId()));
        if (categoryList.get(position).getId()==1||categoryList.get(position).getId()==2||categoryList.get(position).getId()==3){
            myviewHolder.binding.starIconItem.setVisibility(View.VISIBLE);
        }else {
            myviewHolder.binding.starIconItem.setVisibility(View.GONE);
        }


        myviewHolder.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clickHandle.onItemClick(position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    //ربط التصميم
    public class MyviewHolder extends RecyclerView.ViewHolder{

        CategoryViewItemBinding binding;
        public MyviewHolder(CategoryViewItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }



    //اصدار امر عند الضغط على عنصر ولكن من main
    public interface ClickHandle{
        void onItemClick(int position);

    }



}
