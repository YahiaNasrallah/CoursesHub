package com.example.coursesapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.databinding.CourseItemBinding;
import com.example.coursesapp.databinding.LectursItemBinding;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<BookMarks> bookMarksList;


    //استدعاء الinterface
    private BookmarkAdapter.ClickHandle clickHandle;

    CourseItemBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public BookmarkAdapter(Context context, List<BookMarks> bookMarksList, BookmarkAdapter.ClickHandle clickHandle) {
        this.context = context;
        this.bookMarksList = bookMarksList;
        this.clickHandle=clickHandle;
    }

    public void updateLectures(List<BookMarks> newLectures) {
        this.bookMarksList = newLectures;
        notifyDataSetChanged(); // تحديث البيانات وإعادة العرض
    }
    //الدالتين لاعطاء الاوامر للعناصر
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=CourseItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new BookmarkAdapter.MyviewHolder(binding);
    }

    //الوصول لكل عنصر محدد من الليست
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        preferences = context.getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();
        long savedid = preferences.getLong("savedid", 0);

        BookmarkAdapter.MyviewHolder myviewHolder = (BookmarkAdapter.MyviewHolder) holder;
        Appdatabase db = Appdatabase.getDatabase(context.getApplicationContext());
        myviewHolder.binding.tvNamecourseItem.setText(db.courseDao().getCoursesByID(bookMarksList.get(position).getCourseID()).getTitle());


        if (db.courseDao().getCoursesByID(bookMarksList.get(position).getCourseID()).getCategorynameshown().equals("Education")){
            myviewHolder.binding.imageItem.setImageResource(R.drawable.imageoitem2);
        }else if (db.courseDao().getCoursesByID(bookMarksList.get(position).getCourseID()).getCategorynameshown().equals("Engineering")){
            myviewHolder.binding.imageItem.setImageResource(R.drawable.imageoitem3);
        }else if (db.courseDao().getCoursesByID(bookMarksList.get(position).getCourseID()).getCategorynameshown().equals("Business")){
            myviewHolder.binding.imageItem.setImageResource(R.drawable.imageoitem5);

        }else if (db.courseDao().getCoursesByID(bookMarksList.get(position).getCourseID()).getCategorynameshown().equals("Other")){
            myviewHolder.binding.imageItem.setImageResource(R.drawable.imageoitem1);

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
        return bookMarksList.size();
    }


    //ربط التصميم
    public static class MyviewHolder extends RecyclerView.ViewHolder{

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
