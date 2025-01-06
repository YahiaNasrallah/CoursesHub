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

import com.example.coursesapp.databinding.LectursItemBinding;
import com.example.coursesapp.databinding.NotifictionItemBinding;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Notification> notificationList;


    //استدعاء الinterface
    private ClickHandle clickHandle;

    NotifictionItemBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public NotificationAdapter(Context context, List<Notification> notificationList, ClickHandle clickHandle) {
        this.context = context;
        this.notificationList = notificationList;
        this.clickHandle=clickHandle;
    }

    public void updateLectures(List<Notification> newNotification) {
        this.notificationList = newNotification;
        notifyDataSetChanged(); // تحديث البيانات وإعادة العرض
    }
    //الدالتين لاعطاء الاوامر للعناصر
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= NotifictionItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MyviewHolder(binding);
    }

    //الوصول لكل عنصر محدد من الليست
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        preferences = context.getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();
        long savedid = preferences.getLong("savedid", 0);

        MyviewHolder myviewHolder = (MyviewHolder) holder;
        Appdatabase db = Appdatabase.getDatabase(context.getApplicationContext());
        myviewHolder.binding.tvTitileNotificationItem.setText(notificationList.get(position).getTitle());
        myviewHolder.binding.tvMessageNotificationItem.setText(notificationList.get(position).getMessage());









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
        return notificationList.size();
    }


    //ربط التصميم
    public static class MyviewHolder extends RecyclerView.ViewHolder{

        NotifictionItemBinding binding;
        public MyviewHolder(NotifictionItemBinding binding) {
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
