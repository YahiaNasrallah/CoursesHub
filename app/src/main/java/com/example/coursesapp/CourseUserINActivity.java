package com.example.coursesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.example.coursesapp.databinding.ActivityCourseUserInactivityBinding;
import com.google.android.material.tabs.TabLayout;

public class CourseUserINActivity extends AppCompatActivity {

    ActivityCourseUserInactivityBinding binding;
    Appdatabase db;
    long id;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
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
        preferences = getApplicationContext().getSharedPreferences("MyPrefe", Context.MODE_PRIVATE);
        editor = preferences.edit();
        long savedid = preferences.getLong("savedid",0);

        db=Appdatabase.getDatabase(this);
        id=getIntent().getLongExtra("course_id",0);

        Course course=db.courseDao().getCoursesByID(id);

        binding.tvCoursename.setText(db.courseDao().getCoursesByID(course.getId()).getTitle());
        binding.tvDescrition.setText(db.courseDao().getCoursesByID(course.getId()).getDescription());
        binding.tvAuthor.setText(db.courseDao().getCoursesByID(course.getId()).getInstructorName());
        binding.tvLecturesnumber.setText(String.valueOf(db.courseDao().getCoursesByID(course.getId()).getLectureNumber()));




        vpadapter adapter = new vpadapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new CourseDetailsFragment(), "Details");
        adapter.addFragment(new LecturesFragment(), "Lectures");

        binding.pagerdetails.setAdapter(adapter);


        binding.tabLayoutCourses.setupWithViewPager(binding.pagerdetails);




        binding.pagerdetails.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        LecturesFragment fragment = (LecturesFragment) adapter.getFragmentAt(1);
        if (fragment != null) {
            fragment.uUpdateData(course.getId(),savedid,true);
        }

        CourseDetailsFragment fragment2 = (CourseDetailsFragment) adapter.getFragmentAt(0);
        if (fragment2 != null) {
            fragment2.updateData(course.getId(),savedid,false);
        }











    }
}