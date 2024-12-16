package com.example.coursesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coursesapp.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ActivityMainBinding binding;
    Appdatabase db;

    CourseAdapter courseAdapter;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db=Appdatabase.getDatabase(this);
        preferences= getApplicationContext().getSharedPreferences("MyPrefe", Context.MODE_PRIVATE);
        editor=preferences.edit();

//        if (db.categoryDao().getAllCategory().isEmpty()){
//            db.categoryDao().insertCategory(new Category("Education"));
//            db.categoryDao().insertCategory(new Category("Engineering"));
//            db.categoryDao().insertCategory(new Category("Business"));
//        }
//        if (db.courseDao().getAllCourses().isEmpty()){
//            db.courseDao().insertCourse(new Course("first","no","me",db.categoryDao().getCategoryByTitle("Education").getId()));
//
//        }
      //  Toast.makeText(this, String.valueOf(db.courseDao().getAllCourses().size()), Toast.LENGTH_SHORT).show();
//        vpadapter adapter=new vpadapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

//        courseAdapter =new CourseAdapter(this,db.courseDao().getCoursesByCategory(db.categoryDao().getCategoryByTitle("Education").getId()), new CourseAdapter.ClickHandle() {
//            @Override
//            public void onItemClick(int position) {
//
//            }
//
//    });
//
//        binding.recycle.setAdapter(courseAdapter);
//
//
//        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
//        //GridLayoutManager layoutManager=new GridLayoutManager(this,2);
//
//
//        binding.recycle.setLayoutManager(layoutManager);
//
//
//
//
//        binding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (binding.tabLayout.getSelectedTabPosition()==0){
//
//                    Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
//                } else if (binding.tabLayout.getSelectedTabPosition()==1) {
//                    Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
//                } else if (binding.tabLayout.getSelectedTabPosition()==2) {
//                    Toast.makeText(MainActivity.this, "3", Toast.LENGTH_SHORT).show();
//                } else if (binding.tabLayout.getSelectedTabPosition()==3) {
//                    Toast.makeText(MainActivity.this, "4", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//
//
//
//
//
//
//






    }
}

