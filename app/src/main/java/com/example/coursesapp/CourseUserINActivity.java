package com.example.coursesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coursesapp.databinding.ActivityCourseUserInactivityBinding;

public class CourseUserINActivity extends AppCompatActivity {

    ActivityCourseUserInactivityBinding binding;
    Appdatabase db;
    long id;
    LectureAdapter adapter;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityCourseUserInactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db=Appdatabase.getDatabase(this);
        id=getIntent().getLongExtra("course_id",0);

        Course course=db.courseDao().getCoursesByID(id);

        binding.tvCourseTitle.setText(course.getTitle());
        binding.tvProgress.setText(db.myCoursesDao().getMyCourseByCourseID(id).getProgress()+"%");
        binding.progressBar2.setProgress(db.myCoursesDao().getMyCourseByCourseID(id).getProgress());



        binding.backCourseDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseUserINActivity.this, CourseDetailsActivity.class);
                intent.putExtra("course_id",id);
                //لجعل الزر UnVisible في الواجهة
                intent.putExtra("state","out");
                startActivity(intent);
            }
        });

        adapter=new LectureAdapter(this, db.lectureDao().getAllLecturesByCourseID(id), new LectureAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
        adapter.notifyDataSetChanged();
        binding.recycleCourseuserin.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleCourseuserin.setLayoutManager(layoutManager);
        binding.recycleCourseuserin.setHasFixedSize(true);










    }
}