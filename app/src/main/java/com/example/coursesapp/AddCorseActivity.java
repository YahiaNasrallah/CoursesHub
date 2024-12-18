package com.example.coursesapp;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coursesapp.databinding.ActivityAddCorseBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCorseActivity extends AppCompatActivity {

    ActivityAddCorseBinding binding;
    Appdatabase db;
    String Category;
    String LectureNum;
    int LecNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddCorseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = Appdatabase.getDatabase(this);




        binding.main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                binding.main.getWindowVisibleDisplayFrame(rect);

                int screenHeight = binding.main.getRootView().getHeight();
                int keypadHeight = screenHeight - rect.bottom;

                if (keypadHeight > screenHeight * 0.15) { // إذا ظهرت لوحة المفاتيح
                    binding.main.post(() -> binding.main.smoothScrollTo(0, binding.main.getBottom()));
                }
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.lecturenumSpinner.setAdapter(adapter);

        binding.lecturenumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // التعامل مع العنصر الذي تم اختياره
                LectureNum = parentView.getItemAtPosition(position).toString();
                if (LectureNum.equals("1 Lecture")){
                    LecNum=1;
                }else if (LectureNum.equals("2 Lecture")){
                    LecNum=2;
                }else if (LectureNum.equals("3 Lecture")){
                    LecNum=3;
                }else if (LectureNum.equals("4 Lecture")){
                    LecNum=4;
                }else if (LectureNum.equals("5 Lecture")){
                    LecNum=5;
                }else if (LectureNum.equals("6 Lecture")){
                    LecNum=6;
                }
                Toast.makeText(AddCorseActivity.this, String.valueOf(LecNum), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                LecNum=1;
            }
        });






        List<String> data = new ArrayList<>();
        for (int i = 0; i <db.categoryDao().getAllCategory().size() ; i++) {
            data.add(db.categoryDao().getAllCategory().get(i).getCategoryName());
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(adapter2);

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // التعامل مع العنصر الذي تم اختياره
                Category = parentView.getItemAtPosition(position).toString();
                Toast.makeText(AddCorseActivity.this, Category, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            Category="Engineering";
            }
        });




        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edTitle.getText().toString().isEmpty()){
                    binding.edTitle.setError("Enter Title");
                    binding.edTitle.requestFocus();
                } else if (binding.edDetails.getText().toString().isEmpty()) {
                    binding.edDetails.setError("Enter Details");
                    binding.edDetails.requestFocus();
                } else if (binding.edPrice.getText().toString().isEmpty()) {
                    binding.edPrice.setError("Enter Price");
                    binding.edPrice.requestFocus();
                }else if (binding.edHourse.getText().toString().isEmpty()) {
                    binding.edHourse.setError("Enter Hours");
                    binding.edHourse.requestFocus();
                }else if (binding.edInsName.getText().toString().isEmpty()) {
                    binding.edInsName.setError("Enter Instructor Name");
                    binding.edInsName.requestFocus();
                } else if (binding.edDescription.getText().toString().isEmpty()) {
                    binding.edDescription.setError("Enter Description");
                    binding.edDescription.requestFocus();
                } else {
                    Course course=new Course();
                    course.setDescription(binding.edDescription.getText().toString());
                    course.setHours(binding.edHourse.getText().toString());
                    course.setInstructorName(binding.edInsName.getText().toString());
                    course.setPrice(binding.edPrice.getText().toString());
                    course.setTitle(binding.edTitle.getText().toString());
                    course.setLectureNumber(LecNum);
                    course.setDetails(binding.edDetails.getText().toString());
                    course.setNumberOfStudents(0);
                    course.setCategoryID(db.categoryDao().getCategoryByTitle(Category).getId());
                    db.courseDao().insertCourse(course);
                    Toast.makeText(AddCorseActivity.this, "Course Added", Toast.LENGTH_SHORT).show();
                    finish();


                }
            }
        });





    }
}