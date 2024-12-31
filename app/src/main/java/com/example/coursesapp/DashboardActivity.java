package com.example.coursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coursesapp.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    Appdatabase db;
    Animation rotateOpen, rotateClose, fromBottom, toBottom;

    boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db=Appdatabase.getDatabase(this);

        rotateOpen = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.to_bottom_anim);


        binding.btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this, AddEditCorseActivity.class);
                intent.putExtra("zz","new");
                startActivity(intent);
            }
        });
        binding.btnShowAllCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this, ShowAllCoursesActivity.class);
                startActivity(intent);
            }
        });

        binding.btnAddLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this, AddLectureActivity.class);
                intent.putExtra("zz","new");
                startActivity(intent);
            }
        });


        binding.btnMainShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowButtonisClicked();
            }
        });


        binding.btnAddLecturefloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "AddLecture", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnAddCoursefloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "AddCourse", Toast.LENGTH_SHORT).show();
            }
        });







    }

    private void onShowButtonisClicked() {

        setVisibilty(clicked);
        setAnimation(clicked);

        if (!clicked){
            clicked=true;
        }else {
            clicked=false;
        }



    }

    private void setAnimation(boolean clicked) {
        if(!clicked){
            binding.btnAddCoursefloat.setVisibility(View.VISIBLE);
            binding.btnAddLecturefloat.setVisibility(View.VISIBLE);
        }else {
            binding.btnAddCoursefloat.setVisibility(View.INVISIBLE);
            binding.btnAddLecturefloat.setVisibility(View.INVISIBLE);
        }
    }

    private void setVisibilty(boolean clicked) {

         if (!clicked){
             binding.btnAddCoursefloat.startAnimation(fromBottom);
             binding.btnAddLecturefloat.startAnimation(fromBottom);
             binding.btnMainShow.startAnimation(rotateOpen);
         }else {
             binding.btnAddCoursefloat.startAnimation(toBottom);
             binding.btnAddLecturefloat.startAnimation(toBottom);
             binding.btnMainShow.startAnimation(rotateClose);
         }
    }

}