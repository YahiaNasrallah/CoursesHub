package com.example.coursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coursesapp.databinding.ActivityDashboardBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    Appdatabase db;
    Animation rotateOpen, rotateClose, fromBottom, toBottom;
    CourseAdapter adapter;
    int pos;


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
        db = Appdatabase.getDatabase(this);

//        rotateOpen = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.rotate_open_anim);
//        rotateClose = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.rotate_close_anim);
//        fromBottom = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.from_bottom_anim);
//        toBottom = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.to_bottom_anim);
//


        GetAdapterCourse(db.courseDao().getAllCourses());


//        binding.btnAddCourse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(DashboardActivity.this, AddEditCorseActivity.class);
//                intent.putExtra("zz","new");
//                startActivity(intent);
//            }
//        });
//        binding.btnShowAllCourses.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(DashboardActivity.this, ShowAllCoursesActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        binding.btnAddLecture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(DashboardActivity.this, AddLectureActivity.class);
//                intent.putExtra("zz","new");
//                startActivity(intent);
//            }
//        });


        //--------------------AddCourse----------------------------
        binding.btnMainShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onShowButtonisClicked();
                Intent intent = new Intent(DashboardActivity.this, AddCorseActivity.class);
                intent.putExtra("zz", "new");
                startActivity(intent);
            }
        });


        //-----------------Search-------------------------------------

        List<Course> sampleData = db.courseDao().getAllCourses();


        binding.searchViewCourses.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                binding.searchViewCourses.clearFocus();

                return false;
            }
        });

        binding.searchViewCourses.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                List<Course> filteredList = new ArrayList<>();
                for (Course item : sampleData) {
                    if (item.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                GetAdapterCourse(filteredList);


                return false;
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        GetAdapterCourse(db.courseDao().getAllCourses());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        db = Appdatabase.getDatabase(getApplicationContext());
        int id = item.getItemId();

        if (id == R.id.menu_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm the operation?")
                    .setMessage("You Will Delete This Course!")
                    .setPositiveButton("Sure", (dialog, which) -> {
                        long courseId = db.courseDao().getAllCourses().get(pos).getId();

                        if (db.lectureDao().getAllLecturesByCourseID(db.courseDao().getAllCourses().get(pos).getId()).isEmpty()) {
                            db.courseDao().deleteCourse(db.courseDao().getAllCourses().get(pos));
                            GetAdapterCourse(db.courseDao().getAllCourses());
                            db.myCoursesDao().deleteMyCourseByCourseID(courseId);

                        }
                        else {
                            new AlertDialog.Builder(this)
                                    .setTitle("Confirm the operation?")
                                    .setMessage("You Will Delete This Course and Lectures!")
                                    .setPositiveButton("Sure", (dialog2, which2) -> {
                                        db.lectureDao().deleteLecturesByCourseID(courseId);
                                        db.courseDao().deleteCourse(db.courseDao().getAllCourses().get(pos));
                                        db.myCoursesDao().deleteMyCourseByCourseID(courseId);

                                        adapter.notifyItemRemoved(pos);
                                        GetAdapterCourse(db.courseDao().getAllCourses());
                                    })
                                    .setNegativeButton("Cancle", (dialog2, which2) -> dialog2.dismiss())
                                    .show();
                        }
                    })
                    .setNegativeButton("Cancle", (dialog, which) -> dialog.dismiss())
                    .show();
        } else if (id == R.id.menu_edit) {
            Intent intent = new Intent(DashboardActivity.this, DetailsORLecturesActivity.class);
            intent.putExtra("id", db.courseDao().getAllCourses().get(pos).getId());
            startActivity(intent);
        } else if (id==R.id.menu_show_enrolled_users) {
            Intent intent = new Intent(DashboardActivity.this, AdminShowUsersActivity.class);
            intent.putExtra("id", db.courseDao().getAllCourses().get(pos).getId());
            startActivity(intent);






        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflatur=getMenuInflater();
        inflatur.inflate(R.menu.menuadmin,menu);

    }



    void GetAdapterCourse(List<Course> courseList) {

        adapter =new CourseAdapter(this, courseList, new CourseAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {

                Intent intent=new Intent(DashboardActivity.this, DetailsORLecturesActivity.class);;
                intent.putExtra("id",db.courseDao().getAllCourses().get(position).getId());
                startActivity(intent);
            }
            @Override
            public void onLongItemClick(int position) {
                pos = position; // تعيين الموضع لفهرس العنصر
                registerForContextMenu(Objects.requireNonNull(binding.recycleCourses.findViewHolderForAdapterPosition(position)).itemView);            }

        });

        binding.recycleCourses.setAdapter(adapter);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.recycleCourses.setLayoutManager(gridLayoutManager);

    }


}