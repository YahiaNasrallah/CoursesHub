package com.example.coursesapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coursesapp.databinding.ActivityShowAllCategoriesBinding;

import java.util.List;
import java.util.Objects;

public class ShowAllCategoriesCoursesActivity extends AppCompatActivity {

    Appdatabase db;
    ActivityShowAllCategoriesBinding binding;
    CategoryAdapter adapter;
    CourseAdapter adapter2;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityShowAllCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db=Appdatabase.getDatabase(this);


        if (Objects.equals(getIntent().getStringExtra("c"), "category")){
            GetAdapterCategory(db.categoryDao().getAllCategory());



        } else if (Objects.equals(getIntent().getStringExtra("c"), "course")) {
            Toast.makeText(this, String.valueOf(db.courseDao().getAllCourses().size()), Toast.LENGTH_SHORT).show();
            GetAdapterCourse(db.courseDao().getAllCourses());

        }



        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflatur=getMenuInflater();
        inflatur.inflate(R.menu.menu1,menu);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.menu_delete){

            if (db.categoryDao().getCategoryById(id).getId()==1||db.categoryDao().getCategoryById(id).getId()==2||db.categoryDao().getCategoryById(id).getId()==3){
                Toast.makeText(this, "CantDelete", Toast.LENGTH_SHORT).show();
            }else {
                Category category = db.categoryDao().getCategoryById(id);
                db.categoryDao().deleteCategory(category);
                adapter.notifyDataSetChanged();
                GetAdapterCategory(db.categoryDao().getAllCategory());
            }


        }else if (item.getItemId()==R.id.menu_edit) {
            View coustem = LayoutInflater.from(ShowAllCategoriesCoursesActivity.this).inflate(R.layout.log2, null);
            AlertDialog.Builder bulder = new AlertDialog.Builder(this);
            bulder.setView(coustem);
            EditText ed_category_name = coustem.findViewById(R.id.ed_category_name);
            Button btn_ok = coustem.findViewById(R.id.btn_ok);


            AlertDialog dialog = bulder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            ed_category_name.setText(db.categoryDao().getCategoryById(id).getCategoryName());

            if (db.categoryDao().getCategoryById(id).getId() == 1 || db.categoryDao().getCategoryById(id).getId() == 2 || db.categoryDao().getCategoryById(id).getId() == 3) {
                Toast.makeText(this, "CantEdit", Toast.LENGTH_SHORT).show();
            } else {


                dialog.show();
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ed_category_name.getText().toString().isEmpty()) {
                         Toast.makeText(ShowAllCategoriesCoursesActivity.this, "Enter Category Name", Toast.LENGTH_SHORT).show();
                        }else {
                            Category category = db.categoryDao().getCategoryById(id);
                            category.setCategoryName(ed_category_name.getText().toString());
                            db.categoryDao().updateCategory(category);
                            ed_category_name.getText().clear();
                            dialog.hide();
                            adapter.notifyDataSetChanged();
                            GetAdapterCategory(db.categoryDao().getAllCategory());
                        }
                    }
                });
            }
        }
        return super.onContextItemSelected(item);

    }

    void GetAdapterCourse(List<Course> courseList) {

        adapter2=new CourseAdapter(this, courseList, new CourseAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {

            }

        });

                binding.recyclerview.setAdapter(adapter2);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(linearLayoutManager);

    }
    void GetAdapterCategory(List<Category> categoryList){
        adapter=new CategoryAdapter(this, categoryList, new CourseAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {
                registerForContextMenu(binding.recyclerview);
                id=db.categoryDao().getAllCategory().get(position).getId();


            }
        });
        binding.recyclerview.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(linearLayoutManager);

    }


}