package com.example.coursesapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coursesapp.databinding.ActivityAdminShowUsersBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminShowUsersActivity extends AppCompatActivity {

    ActivityAdminShowUsersBinding binding;
    Appdatabase db;
    UserAdapter adapter;
    List<User> OldestToNewestlist;
    List<User> NewestToOldestlist;
    List<User> OngoingList;
    List<User> CompletedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminShowUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        long courseid = getIntent().getLongExtra("id", 0);
        db = Appdatabase.getDatabase(this);

        // إعداد القوائم
        initializeLists(courseid);
        getAdapter(NewestToOldestlist);
        binding.tvUser.setText("Users ("+NewestToOldestlist.size()+")");


        View customView = LayoutInflater.from(this).inflate(R.layout.sortby_item, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        RadioButton rb_newest = customView.findViewById(R.id.rb_nto);
        rb_newest.setChecked(true);

        RadioGroup rg_sort = customView.findViewById(R.id.rg_sort);
        binding.btnFilter.setOnClickListener(view -> {
            dialog.show();
            rg_sort.setOnCheckedChangeListener((radioGroup, i) -> {
                if (R.id.rb_ongoing == i) {
                    getAdapter(OngoingList);
                    binding.tvUser.setText("Users ("+OngoingList.size()+")");
                } else if (R.id.rb_complete == i) {
                    getAdapter(CompletedList);
                    binding.tvUser.setText("Users ("+CompletedList.size()+")");
                } else if (R.id.rb_nto == i) {
                    getAdapter(NewestToOldestlist);
                    binding.tvUser.setText("Users ("+NewestToOldestlist.size()+")");
                } else if (R.id.rb_otn == i) {
                    getAdapter(OldestToNewestlist);
                    binding.tvUser.setText("Users ("+OldestToNewestlist.size()+")");
                }
                dialog.dismiss();
            });
        });
    }

    private void initializeLists(long courseid) {
        OldestToNewestlist = new ArrayList<>();
        OngoingList = new ArrayList<>();
        CompletedList = new ArrayList<>();

        List<MyCourses> myCourses = db.myCoursesDao().getAllMyCourseByCourseID(courseid);

        for (MyCourses course : myCourses) {
            User user = db.userDao().getUserByid(course.getUserID());
            OldestToNewestlist.add(user);
            if (course.isCompleted()) {
                CompletedList.add(user);
            } else {
                OngoingList.add(user);
            }
        }

        NewestToOldestlist = new ArrayList<>(OldestToNewestlist);
        Collections.reverse(NewestToOldestlist);
    }

    private void getAdapter(List<User> list) {
        long courseid = getIntent().getLongExtra("id", 0);
        adapter = new UserAdapter(this, list, courseid, new UserAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {

            }
        });

                binding.recycleUsers.setAdapter(adapter);
        binding.recycleUsers.setLayoutManager(new LinearLayoutManager(this));
    }
}
