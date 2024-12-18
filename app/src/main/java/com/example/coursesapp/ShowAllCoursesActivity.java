package com.example.coursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coursesapp.databinding.ActivityShowAllCategoriesBinding;

import java.util.List;

public class ShowAllCoursesActivity extends AppCompatActivity {

    Appdatabase db;
    ActivityShowAllCategoriesBinding binding;
    CourseAdapter adapter;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityShowAllCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db=Appdatabase.getDatabase(this);


        GetAdapterCourse(db.courseDao().getAllCourses());





        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void GetAdapterCourse(List<Course> courseList) {

        adapter =new CourseAdapter(this, courseList, new CourseAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {

                Intent intent=new Intent(ShowAllCoursesActivity.this, AddEditCorseActivity.class);;
                intent.putExtra("zz","edit");
                intent.putExtra("id",String.valueOf(db.courseDao().getAllCourses().get(position).getId()));
                startActivity(intent);
            }

        });

                binding.recyclerview.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(linearLayoutManager);

    }


}