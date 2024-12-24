package com.example.coursesapp;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCourseDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db=Appdatabase.getDatabase(this);
        preferences = getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();


       // String savedEmail = preferences.getString("savedName", null); // استرجاع البريد الإلكتروني
       // String savedPassword = preferences.getString("savedPAssword", null);
//
//        if (savedPassword != null) {
//            // قم باستخدام البريد الإلكتروني (على سبيل المثال: عرض البريد في TextView)
//        } else {
//            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
//        }



        long savedid = preferences.getLong("savedid",0);


        if (Objects.equals(getIntent().getStringExtra("state"), "out")){
            binding.btnSigntocourse.setVisibility(View.GONE);

        }else {
            binding.btnSigntocourse.setVisibility(View.VISIBLE);
        }

        //Toast.makeText(this, savedPassword, Toast.LENGTH_SHORT).show();
        User user=db.userDao().getUserByid(savedid);

        //Toast.makeText(this, String.valueOf(user.getUsername()), Toast.LENGTH_SHORT).show();

        Course course=db.courseDao().getCoursesByID((getIntent().getLongExtra("course_id",0)));

        binding.tvCourseTitle.setText(course.getTitle());
        binding.tvInstructorName.setText(course.getInstructorName());
        binding.tvCourseDescription.setText(course.getDescription());
        binding.tvCoursePrice.setText(course.getPrice());
        binding.tvCourseHours.setText(course.getHours());
        binding.tvCourseLectures.setText(String.valueOf(course.getLectureNumber()));
        binding.tvCourseDetails.setText(course.getDetails());
        binding.tvCourseNumStuent.setText(String.valueOf(db.myCoursesDao().getAllMyCourses(savedid).size()));
        //binding.courseImage.setImageResource(course.getImage());
        //binding.courseImage.setImageResource(course.getImage());


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.btnSigntocourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean bb=true;

                for (int i = 0; i <db.myCoursesDao().getAllMyCourses(savedid).size() ; i++) {
                    if (db.myCoursesDao().getAllMyCourses(savedid).get(i).getCourseID()==course.getId()&&db.myCoursesDao().getAllMyCourses(savedid).get(i).getUserID()==user.getId()){
                        Toast.makeText(CourseDetailsActivity.this, "You are already sign this course", Toast.LENGTH_SHORT).show();
                        bb=false;
                    }

                }
                if (bb) {
                    MyCourses myCourses = new MyCourses();
                    myCourses.setCourseID(course.getId());
                    myCourses.setUserID(user.getId());
                    myCourses.setProgress(0);
                    myCourses.setCompleted(false);
                    db.myCoursesDao().insertMyCourse(myCourses);
                    Toast.makeText(CourseDetailsActivity.this, "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });








    }
}