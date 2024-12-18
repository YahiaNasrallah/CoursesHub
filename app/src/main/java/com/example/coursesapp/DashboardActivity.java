package com.example.coursesapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coursesapp.databinding.ActivityDashboardBinding;

import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    Appdatabase db;
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

        View coustem= LayoutInflater.from(DashboardActivity.this).inflate(R.layout.log2,null);
        AlertDialog.Builder bulder=new AlertDialog.Builder(this);
        bulder.setView(coustem);
        EditText ed_category_name=coustem.findViewById(R.id.ed_category_name);
        Button btn_ok=coustem.findViewById(R.id.btn_ok);




        AlertDialog dialog=bulder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        binding.btnAddcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_category_name.getText().toString().isEmpty()){
                    Toast.makeText(DashboardActivity.this, "Enter Category Name", Toast.LENGTH_SHORT).show();
                }else {
                    if(db.categoryDao().getCategoryByName(ed_category_name.getText().toString().toLowerCase(Locale.ROOT).toLowerCase())){
                        Toast.makeText(DashboardActivity.this, "Category Already Exist", Toast.LENGTH_SHORT).show();
                    }else {
                    db.categoryDao().insertCategory(new Category(ed_category_name.getText().toString()));
                    ed_category_name.getText().clear();
                    dialog.hide();
                }
                }
            }
        });

        binding.btnShowAllcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this, ShowAllCategoriesCoursesActivity.class);
                intent.putExtra("c","category");
                startActivity(intent);
            }
        });

        binding.btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, AddCorseActivity.class));
            }
        });
        binding.btnShowAllCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this, ShowAllCategoriesCoursesActivity.class);
                intent.putExtra("c","course");
                startActivity(intent);
            }
        });



    }
}