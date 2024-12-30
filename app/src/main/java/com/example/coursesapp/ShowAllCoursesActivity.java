package com.example.coursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coursesapp.databinding.ActivityShowAllCoursesBinding;

import java.util.List;
import java.util.Objects;

public class ShowAllCoursesActivity extends AppCompatActivity {

    Appdatabase db;
    ActivityShowAllCoursesBinding binding;
    CourseAdapter adapter;
    int pos;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityShowAllCoursesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db=Appdatabase.getDatabase(this);


        GetAdapterCourse(db.courseDao().getAllCourses());





        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void GetAdapterCourse(List<Course> courseList) {

        adapter =new CourseAdapter(this, courseList, new CourseAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {

                pos=position;
                Intent intent=new Intent(ShowAllCoursesActivity.this, AddEditCorseActivity.class);;
                intent.putExtra("zz","edit");
                intent.putExtra("id",String.valueOf(db.courseDao().getAllCourses().get(position).getId()));
                startActivity(intent);
            }
            @Override
            public void onLongItemClick(int position) {

                registerForContextMenu(binding.recyclerview);
            }

        });

                binding.recyclerview.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflatur=getMenuInflater();
        inflatur.inflate(R.menu.menu1,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        db=Appdatabase.getDatabase(getApplicationContext());

        int id=item.getItemId();
        if (id==R.id.menu_delete){
            new AlertDialog.Builder(this)
                    .setTitle("تأكيد العملية")
                    .setMessage("هل أنت متأكد أنك تريد المتابعة؟")
                    .setPositiveButton("نعم", (dialog, which) -> {

                        if (db.lectureDao().getLectureByID(db.courseDao().getAllCourses().get(pos).getId())==null) {
                            db.courseDao().deleteCourse(db.courseDao().getAllCourses().get(pos));
                            adapter.notifyItemRemoved(pos);
                            GetAdapterCourse(db.courseDao().getAllCourses());
                        } else {
                            new AlertDialog.Builder(this)
                                    .setTitle("تأكيد العملية")
                                    .setMessage("هل تريد حذف الدورة و المحاضرات؟")
                                    .setPositiveButton("نعم", (dialog2, which2) -> {

                                        db=Appdatabase.getDatabase(getApplicationContext());

                                        long courseId = db.courseDao().getAllCourses().get(pos).getId();

// حذف المحاضرات المرتبطة بالدورة
                                        db.lectureDao().deleteLecturesByCourseID(courseId);

// حذف الدورة
                                        db.courseDao().deleteCourse(db.courseDao().getAllCourses().get(pos));

                                        adapter.notifyItemRemoved(pos);
                                        GetAdapterCourse(db.courseDao().getAllCourses());




                                        GetAdapterCourse(db.courseDao().getAllCourses());
                                    })
                                    .setNegativeButton("لا", (dialog2, which2) -> {
                                        dialog.dismiss();
                                        Toast.makeText(this, "تم الإلغاء", Toast.LENGTH_SHORT).show();
                                    }).show();


                        }

                        adapter.notifyItemRemoved(pos);

                        GetAdapterCourse(db.courseDao().getAllCourses());
                    })
                    .setNegativeButton("لا", (dialog, which) -> {
                        dialog.dismiss();
                        Toast.makeText(this, "تم الإلغاء", Toast.LENGTH_SHORT).show();
                    })

                    .show();


        } else if (id==R.id.menu_edit) {
            Intent intent = new Intent(ShowAllCoursesActivity.this, AddEditCorseActivity.class);
            intent.putExtra("zz", "edit");
            intent.putExtra("id",String.valueOf(db.courseDao().getAllCourses().get(pos).getId()));
            startActivity(intent);

        }
        return super.onContextItemSelected(item);

    }

    @Override
    protected void onResume() {
        GetAdapterCourse(db.courseDao().getAllCourses());
        super.onResume();
    }
}