package com.example.coursesapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.coursesapp.databinding.ActivityCourseDetailsBinding;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.Objects;

public class CourseDetailsActivity extends AppCompatActivity {

    ActivityCourseDetailsBinding binding;
    Appdatabase db;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    long id;
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityCourseDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db=Appdatabase.getDatabase(this);
        preferences = getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();


        id=getIntent().getLongExtra("course_id",0);


        long savedid = preferences.getLong("savedid",0);
        User user=db.userDao().getUserByid(savedid);



        Course course=db.courseDao().getCoursesByID(id);





         if (Objects.equals(getIntent().getStringExtra("from"), "home")){

             binding.tvCoursename.setText(course.getTitle());
             binding.tvDescrition.setText(course.getDescription());
             loadImageFromStorage(course.getImagePath(),binding.imageViewCourse);

             // إنشاء الـ Adapter وإضافة الفراجمنتات
             vpadapter adapter = new vpadapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
             adapter.addFragment(new CourseDetailsFragment(), "Details");
             adapter.addFragment(new LecturesFragment(), "Lectures");

             binding.pagerdetails.setAdapter(adapter);


             binding.tabLayoutCourses.setupWithViewPager(binding.pagerdetails);

             CourseDetailsFragment fragment = (CourseDetailsFragment) adapter.getFragmentAt(0);
             if (fragment != null) {
                 fragment.updateData(course.getId(),user.getId(),true);
             }



             boolean isBookmarked = false;
             for (BookMarks bookmark : db.bookMarksDao().getAllBookMarks(user.getId())) {
                 if (bookmark.getCourseID() == course.getId()) {
                     isBookmarked = true;
                     break;
                 }
             }

             if (isBookmarked) {
                 binding.imageBookmark.setImageResource(R.drawable.baseline_bookmarks_24);
             }
             else {
                 binding.imageBookmark.setImageResource(R.drawable.baseline_bookmark_border_24);
             }



             binding.cardAddTobookmark.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     // التحقق من وجود العنصر في الإشارات المرجعية
                     boolean isBookmarked = false;
                     for (BookMarks bookmark : db.bookMarksDao().getAllBookMarks(user.getId())) {
                         if (bookmark.getCourseID() == course.getId()) {
                             isBookmarked = true;
                             break;
                         }
                     }

                     if (isBookmarked) {
                         // حذف الإشارة المرجعية
                         BookMarks bookmarkToRemove = db.bookMarksDao().getBookmarkByCourseAndUser(course.getId(), user.getId());
                         db.bookMarksDao().delete(bookmarkToRemove);
                         binding.imageBookmark.setImageResource(R.drawable.baseline_bookmark_border_24);
                     } else {
                         // إضافة الإشارة المرجعية
                         BookMarks newBookmark = new BookMarks(course.getId(), user.getId());
                         db.bookMarksDao().insertBookMark(newBookmark);
                         binding.imageBookmark.setImageResource(R.drawable.baseline_bookmarks_24);
                     }
                 }
             });



         }

         else if (Objects.equals(getIntent().getStringExtra("from"), "mycourses")) {

             binding.tvCoursename.setText(course.getTitle());
             binding.tvDescrition.setText(course.getDescription());
             loadImageFromStorage(course.getImagePath(),binding.imageViewCourse);


             vpadapter adapter = new vpadapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
             adapter.addFragment(new LecturesFragment(), "Lectures");
             adapter.addFragment(new CourseDetailsFragment(), "Details");

             binding.pagerdetails.setAdapter(adapter);


             binding.tabLayoutCourses.setupWithViewPager(binding.pagerdetails);


             LecturesFragment fragment = (LecturesFragment) adapter.getFragmentAt(0);
        if (fragment != null) {
            fragment.uUpdateData(course.getId(),savedid,true);
        }

        CourseDetailsFragment fragment2 = (CourseDetailsFragment) adapter.getFragmentAt(1);
        if (fragment2 != null) {
            fragment2.updateData(course.getId(),savedid,false);
        }
         }
        boolean isBookmarked = false;
        for (BookMarks bookmark : db.bookMarksDao().getAllBookMarks(user.getId())) {
            if (bookmark.getCourseID() == course.getId()) {
                isBookmarked = true;
                break;
            }
        }

        if (isBookmarked) {
            binding.imageBookmark.setImageResource(R.drawable.baseline_bookmarks_24);
        }
        else {
            binding.imageBookmark.setImageResource(R.drawable.baseline_bookmark_border_24);
        }



        binding.cardAddTobookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // التحقق من وجود العنصر في الإشارات المرجعية
                boolean isBookmarked = false;
                for (BookMarks bookmark : db.bookMarksDao().getAllBookMarks(user.getId())) {
                    if (bookmark.getCourseID() == course.getId()) {
                        isBookmarked = true;
                        break;
                    }
                }

                if (isBookmarked) {
                    // حذف الإشارة المرجعية
                    BookMarks bookmarkToRemove = db.bookMarksDao().getBookmarkByCourseAndUser(course.getId(), user.getId());
                    db.bookMarksDao().delete(bookmarkToRemove);
                    binding.imageBookmark.setImageResource(R.drawable.baseline_bookmark_border_24);
                } else {
                    // إضافة الإشارة المرجعية
                    BookMarks newBookmark = new BookMarks(course.getId(), user.getId());
                    db.bookMarksDao().insertBookMark(newBookmark);
                    binding.imageBookmark.setImageResource(R.drawable.baseline_bookmarks_24);
                }
            }
        });





    }
    public void loadImageFromStorage(String imagePath, ImageView imageView) {
        File imgFile = new  File(imagePath);
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
    }
}