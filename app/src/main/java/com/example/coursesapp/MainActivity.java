package com.example.coursesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.coursesapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        db = Appdatabase.getDatabase(this);
        preferences = getApplicationContext().getSharedPreferences("MyPrefe", Context.MODE_PRIVATE);
        editor = preferences.edit();

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(binding.navbottom, new androidx.core.view.OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                // إلغاء الحشوة الإضافية من النظام
                return insets.replaceSystemWindowInsets(
                        insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        0 // حشوة سفلية صفرية
                );
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.navbottom);
        bottomNavigationView.setItemRippleColor(ColorStateList.valueOf(getColor(R.color.black)));


        bottomNavigationView.setItemRippleColor(null);



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
        binding.pagerMain.setOffscreenPageLimit(4); // حافظ على 3 صفحات في الذاكرة

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        binding.pagerMain.setAdapter(adapter);

        // التحكم في BottomNavigationView لتغيير الـ ViewPager
        binding.navbottom.setOnItemSelectedListener(item -> {
            if (item.getItemId()==R.id.nav_home_item){
                binding.pagerMain.setCurrentItem(0);

            }else if (item.getItemId()==R.id.nav_mycourses_item){
                binding.pagerMain.setCurrentItem(1);

            } else if (item.getItemId()==R.id.nav_bookmarks_item) {
                binding.pagerMain.setCurrentItem(2);

            } else if (item.getItemId()==R.id.nav_profile_item){
                binding.pagerMain.setCurrentItem(3);

            }

            return true;
        });

        // التحكم في ViewPager لتغيير العناصر في BottomNavigation
        binding.pagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.navbottom.setSelectedItemId(R.id.nav_home_item);
                        break;
                    case 1:
                        binding.navbottom.setSelectedItemId(R.id.nav_mycourses_item);
                        break;
                    case 2:
                        binding.navbottom.setSelectedItemId(R.id.nav_bookmarks_item);
                        break;
                    case 3:
                        binding.navbottom.setSelectedItemId(R.id.nav_profile_item);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}