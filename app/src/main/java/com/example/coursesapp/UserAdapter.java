package com.example.coursesapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.databinding.CategoryViewItemBinding;
import com.example.coursesapp.databinding.UserItemBinding;

import java.io.File;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<User> userList;


    //استدعاء الinterface
    private UserAdapter.ClickHandle clickHandle;

    long courseid;

    UserItemBinding binding;


    public UserAdapter(Context context, List<User> userList,long courseid, UserAdapter.ClickHandle clickHandle) {
        this.context = context;
        this.userList = userList;
        this.clickHandle=clickHandle;
        this.courseid=courseid;
    }


    //الدالتين لاعطاء الاوامر للعناصر
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=UserItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new UserAdapter.MyviewHolder(binding);
    }

    //الوصول لكل عنصر محدد من الليست
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Appdatabase db=Appdatabase.getDatabase(context);
        UserAdapter.MyviewHolder myviewHolder= (UserAdapter.MyviewHolder) holder;

        myviewHolder.binding.tvUserNameItemuser.setText(userList.get(position).getFirstName()+" "+userList.get(position).getLastName());
        loadImageFromStorage(userList.get(position).getUserImagePath(),myviewHolder.binding.imageUserProfileUseritem);

        if (db.myCoursesDao().getMyCourseByCourseIDAndUserID(userList.get(position).getId(),courseid)!=null){
            if (db.myCoursesDao().getMyCourseByCourseIDAndUserID(userList.get(position).getId(),courseid).isCompleted()){

                MyCourses myCourses=db.myCoursesDao().getMyCourseByCourseIDAndUserID(userList.get(position).getId(),courseid);

                myviewHolder.binding.tvStatusItemuser.setText("Complete");
                myviewHolder.binding.tvJoinedDateItemuser.setText("End Date: "+myCourses.getCompleteDate());
                myviewHolder.binding.tvStatusItemuser.setTextColor(context.getResources().getColor(R.color.gren));

            }else{
                MyCourses myCourses=db.myCoursesDao().getMyCourseByCourseIDAndUserID(userList.get(position).getId(),courseid);
                myviewHolder.binding.tvJoinedDateItemuser.setText("Start Date: "+myCourses.getStartDate());
                myviewHolder.binding.tvStatusItemuser.setText("Ongoing");
            }

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
        return userList.size();
    }


    //ربط التصميم
    public class MyviewHolder extends RecyclerView.ViewHolder{

        UserItemBinding binding;
        public MyviewHolder(UserItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }



    //اصدار امر عند الضغط على عنصر ولكن من main
    public interface ClickHandle{
        void onItemClick(int position);

    }
    public void loadImageFromStorage(String imagePath, ImageView imageView) {
        File imgFile = new  File(imagePath);
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
    }


}
