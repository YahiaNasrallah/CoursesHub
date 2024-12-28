package com.example.coursesapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.coursesapp.databinding.ActivityCourseDetailsBinding;
import com.google.android.material.tabs.TabLayout;

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


//        if (Objects.equals(getIntent().getStringExtra("state"), "out")){
//            binding.btnSigntocourse.setVisibility(View.GONE);
//
//        }else {
//            binding.btnSigntocourse.setVisibility(View.VISIBLE);
//        }

        //Toast.makeText(this, savedPassword, Toast.LENGTH_SHORT).show();
        User user=db.userDao().getUserByid(savedid);

        //Toast.makeText(this, String.valueOf(user.getUsername()), Toast.LENGTH_SHORT).show();

        Course course=db.courseDao().getCoursesByID((getIntent().getLongExtra("course_id",0)));
//
//        binding.tvCourseTitle.setText(course.getTitle());
//        binding.tvInstructorName.setText(course.getInstructorName());
//        binding.tvCourseDescription.setText(course.getDescription());
//        binding.tvCoursePrice.setText(course.getPrice());
//        binding.tvCourseHours.setText(course.getHours());
//        binding.tvCourseLectures.setText(String.valueOf(course.getLectureNumber()));
//        binding.tvCourseDetails.setText(course.getDetails());
//        binding.tvCourseNumStuent.setText(String.valueOf(db.myCoursesDao().getAllMyCourses(savedid).size()));
//        //binding.courseImage.setImageResource(course.getImage());
//        //binding.courseImage.setImageResource(course.getImage());


//        binding.backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//
//        binding.btnSigntocourse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean bb=true;
//
//                for (int i = 0; i <db.myCoursesDao().getAllMyCourses(savedid).size() ; i++) {
//                    if (db.myCoursesDao().getAllMyCourses(savedid).get(i).getCourseID()==course.getId()&&db.myCoursesDao().getAllMyCourses(savedid).get(i).getUserID()==user.getId()){
//                        Toast.makeText(CourseDetailsActivity.this, "You are already sign this course", Toast.LENGTH_SHORT).show();
//                        bb=false;
//                    }
//
//                }
//                if (bb) {
//                    MyCourses myCourses = new MyCourses();
//                    myCourses.setCourseID(course.getId());
//                    myCourses.setUserID(user.getId());
//                    myCourses.setProgress(0);
//                    myCourses.setCompleted(false);
//                    db.myCoursesDao().insertMyCourse(myCourses);
//                    Toast.makeText(CourseDetailsActivity.this, "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//
//            }
//        });
//

        binding.tvCoursename.setText(db.courseDao().getCoursesByID(course.getId()).getTitle());
        binding.tvDescrition.setText(db.courseDao().getCoursesByID(course.getId()).getDescription());
        binding.tvAuthor.setText(db.courseDao().getCoursesByID(course.getId()).getInstructorName());
        binding.tvLecturesnumber.setText(String.valueOf(db.courseDao().getCoursesByID(course.getId()).getLectureNumber()));


        TabLayout tabLayout = findViewById(R.id.tab_layout_courses);
         ViewPager viewPager = findViewById(R.id.pagerdetails);

        // إنشاء الـ Adapter وإضافة الفراجمنتات
        vpadapter adapter = new vpadapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new CourseDetailsFragment(), "Details");
        adapter.addFragment(new LecturesFragment(), "Lectures");

        viewPager.setAdapter(adapter);


        tabLayout.setupWithViewPager(viewPager);




        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        CourseDetailsFragment fragment = (CourseDetailsFragment) adapter.getFragmentAt(0);
        if (fragment != null) {
            fragment.updateData(course.getId(),user.getId(),true);
        }










    }

}