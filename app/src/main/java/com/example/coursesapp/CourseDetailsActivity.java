package com.example.coursesapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coursesapp.databinding.ActivityCourseDetailsBinding;

import java.util.Objects;

public class CourseDetailsActivity extends AppCompatActivity {

    ActivityCourseDetailsBinding binding;
    Appdatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCourseDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db=Appdatabase.getDatabase(this);

        Course course=db.courseDao().getCoursesByID((getIntent().getLongExtra("course_id",0)));

        binding.tvCourseTitle.setText(course.getTitle());
        binding.tvInstructorName.setText(course.getInstructorName());
        binding.tvCourseDescription.setText(course.getDescription());
        binding.tvCoursePrice.setText(course.getPrice());
        binding.tvCourseHours.setText(course.getHours());
        binding.tvCourseLectures.setText(String.valueOf(course.getLectureNumber()));
        binding.tvCourseDetails.setText(course.getDetails());
        //binding.courseImage.setImageResource(course.getImage());











    }
}