package com.example.coursesapp;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.databinding.ActivityAddCorseBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddEditCorseActivity extends AppCompatActivity {

    ActivityAddCorseBinding binding;
    Appdatabase db;
    String Category;
    String LectureNum;
    ArrayList<User> TempDeletedUser;
    int LecNum;
    int pos;
    boolean flag=false;
    LectureAdapter adapter;

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


        if (Objects.equals(getIntent().getStringExtra("zz"), "new")) {

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
                    if (LectureNum.equals("1 Lecture")) {
                        LecNum = 1;
                    } else if (LectureNum.equals("2 Lecture")) {
                        LecNum = 2;
                    } else if (LectureNum.equals("3 Lecture")) {
                        LecNum = 3;
                    } else if (LectureNum.equals("4 Lecture")) {
                        LecNum = 4;
                    } else if (LectureNum.equals("5 Lecture")) {
                        LecNum = 5;
                    } else if (LectureNum.equals("6 Lecture")) {
                        LecNum = 6;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    LecNum = 1;
                }
            });


            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.spinner_items_categories, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.categorySpinner.setAdapter(adapter2);


            binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // التعامل مع العنصر الذي تم اختياره
                    Category = parentView.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    Category = "Other";
                }
            });


            binding.btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (binding.edTitle.getText().toString().isEmpty()) {
                        binding.edTitle.setError("Enter Title");
                        binding.edTitle.requestFocus();
                    } else if (binding.edDetails.getText().toString().isEmpty()) {
                        binding.edDetails.setError("Enter Details");
                        binding.edDetails.requestFocus();
                    } else if (binding.edPrice.getText().toString().isEmpty()) {
                        binding.edPrice.setError("Enter Price");
                        binding.edPrice.requestFocus();
                    } else if (binding.edHourse.getText().toString().isEmpty()) {
                        binding.edHourse.setError("Enter Hours");
                        binding.edHourse.requestFocus();
                    } else if (binding.edInsName.getText().toString().isEmpty()) {
                        binding.edInsName.setError("Enter Instructor Name");
                        binding.edInsName.requestFocus();
                    } else if (binding.edDescription.getText().toString().isEmpty()) {
                        binding.edDescription.setError("Enter Description");
                        binding.edDescription.requestFocus();
                    } else {
                        Course course = new Course();
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
                        Toast.makeText(AddEditCorseActivity.this, "Course Added", Toast.LENGTH_SHORT).show();
                        Toast.makeText(AddEditCorseActivity.this, Category, Toast.LENGTH_SHORT).show();
                        finish();


                    }
                }
            });


        }




        else if (Objects.equals(getIntent().getStringExtra("zz"), "edit")) {




            binding.lectursBtn.setVisibility(View.VISIBLE);
            binding.lectursBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!flag){
                        binding.recycleLectures.setVisibility(View.GONE);
                        binding.linAdd.setVisibility(View.VISIBLE);
                        flag=true;
                    }else {
                        binding.recycleLectures.setVisibility(View.VISIBLE);
                        binding.linAdd.setVisibility(View.GONE);
                        flag=false;
                    }

                    List<Lecture> lectureList = db.lectureDao().getAllLecturesByCourseID(
                            Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("id"))));


                        // إعداد Adapter وإرفاقه بـ RecyclerView
                        adapter = new LectureAdapter(AddEditCorseActivity.this, lectureList, new LectureAdapter.ClickHandle() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent=new Intent(AddEditCorseActivity.this, AddLectureActivity.class);
                                intent.putExtra("zz","edit");
                                intent.putExtra("d1",String.valueOf(db.lectureDao().getAllLecturesByCourseID(Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("id")))).get(pos).getId()));
                                intent.putExtra("d2",pos);
                                startActivity(intent);                            }

                            @Override
                            public void onLongItemClick(int position) {
                                 pos = position;
                                registerForContextMenu(binding.recycleLectures);

                            }
                        });

                        binding.recycleLectures.setAdapter(adapter); // ربط RecyclerView مع الـ Adapter
                        binding.recycleLectures.setLayoutManager(new LinearLayoutManager(AddEditCorseActivity.this));

                }
            });


            Course course = db.courseDao().getCoursesByID(Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("id"))));
            binding.edTitle.setText(course.getTitle());
            binding.edDetails.setText(course.getDetails());
            binding.edPrice.setText(course.getPrice());
            binding.edHourse.setText(course.getHours());
            binding.edInsName.setText(course.getInstructorName());
            binding.edDescription.setText(course.getDescription());


            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.spinner_items, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.lecturenumSpinner.setAdapter(adapter);

            binding.lecturenumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // التعامل مع العنصر الذي تم اختياره
                    LectureNum = parentView.getItemAtPosition(position).toString();
                    if (LectureNum.equals("1 Lecture")) {
                        LecNum = 1;
                    } else if (LectureNum.equals("2 Lecture")) {
                        LecNum = 2;
                    } else if (LectureNum.equals("3 Lecture")) {
                        LecNum = 3;
                    } else if (LectureNum.equals("4 Lecture")) {
                        LecNum = 4;
                    } else if (LectureNum.equals("5 Lecture")) {
                        LecNum = 5;
                    } else if (LectureNum.equals("6 Lecture")) {
                        LecNum = 6;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    LecNum = 1;
                }
            });


            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.spinner_items_categories, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.categorySpinner.setAdapter(adapter2);
            binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // التعامل مع العنصر الذي تم اختياره
                    Category = parentView.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    Category = "Other";
                }
            });


            binding.lecturenumSpinner.setSelection(course.getLectureNumber() - 1);
            if (db.categoryDao().getCategoryById(course.getCategoryID()).getCategoryName().equals("Education")) {
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                binding.categorySpinner.setSelection(0);
            } else if (db.categoryDao().getCategoryById(course.getCategoryID()).getCategoryName().equals("Engineering")) {
                binding.categorySpinner.setSelection(1);
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
            } else if (db.categoryDao().getCategoryById(course.getCategoryID()).getCategoryName().equals("Business")) {
                binding.categorySpinner.setSelection(2);
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
            } else {
                binding.categorySpinner.setSelection(3);
                Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
            }

            binding.btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (binding.edTitle.getText().toString().isEmpty()) {
                        binding.edTitle.setError("Enter Title");
                        binding.edTitle.requestFocus();
                    } else if (binding.edDetails.getText().toString().isEmpty()) {
                        binding.edDetails.setError("Enter Details");
                        binding.edDetails.requestFocus();
                    } else if (binding.edPrice.getText().toString().isEmpty()) {
                        binding.edPrice.setError("Enter Price");
                        binding.edPrice.requestFocus();
                    } else if (binding.edHourse.getText().toString().isEmpty()) {
                        binding.edHourse.setError("Enter Hours");
                        binding.edHourse.requestFocus();
                    } else if (binding.edInsName.getText().toString().isEmpty()) {
                        binding.edInsName.setError("Enter Instructor Name");
                        binding.edInsName.requestFocus();
                    } else if (binding.edDescription.getText().toString().isEmpty()) {
                        binding.edDescription.setError("Enter Description");
                        binding.edDescription.requestFocus();
                    } else {
                        Course course = new Course();
                        course.setDescription(binding.edDescription.getText().toString());
                        course.setHours(binding.edHourse.getText().toString());
                        course.setInstructorName(binding.edInsName.getText().toString());
                        course.setPrice(binding.edPrice.getText().toString());
                        course.setTitle(binding.edTitle.getText().toString());
                        course.setLectureNumber(LecNum);
                        course.setDetails(binding.edDetails.getText().toString());
                        course.setNumberOfStudents(0);
                        course.setCategoryID(db.categoryDao().getCategoryByTitle(Category).getId());
                        course.setId(Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("id"))));
                        db.courseDao().updateCourse(course);
                        Toast.makeText(AddEditCorseActivity.this, "Course Updated", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }
            });
        }


    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflatur=getMenuInflater();
        inflatur.inflate(R.menu.menu1,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.menu_delete){

            new AlertDialog.Builder(this)
                    .setTitle("تأكيد العملية")
                    .setMessage("هل أنت متأكد أنك تريد المتابعة؟")
                    .setPositiveButton("نعم", (dialog, which) -> {
                        Lecture lecture=db.lectureDao().getAllLecturesByCourseID(Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("id")))).get(pos);
                        db.lectureDao().deleteLecture(lecture);
                        adapter.notifyDataSetChanged();
                        adapter = new LectureAdapter(AddEditCorseActivity.this,db.lectureDao().getAllLecturesByCourseID(
                                Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("id")))) , new LectureAdapter.ClickHandle() {
                            @Override
                            public void onItemClick(int position) {
                                Toast.makeText(AddEditCorseActivity.this, "Clicked position: " + position, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLongItemClick(int position) {
                                pos = position;
                                registerForContextMenu(binding.recycleLectures);
                            }
                        });
                        binding.recycleLectures.setAdapter(adapter); // ربط RecyclerView مع الـ Adapter
                        binding.recycleLectures.setLayoutManager(new LinearLayoutManager(AddEditCorseActivity.this));

                        Toast.makeText(this, "Lecture Deleted", Toast.LENGTH_SHORT).show();

                    })
                    .setNegativeButton("لا", (dialog, which) -> {
                        dialog.dismiss();

                    })
                    .show();

        } else if (item.getItemId()==R.id.menu_edit) {
            Intent intent=new Intent(AddEditCorseActivity.this, AddLectureActivity.class);
            intent.putExtra("zz","edit");
            intent.putExtra("d1",String.valueOf(db.lectureDao().getAllLecturesByCourseID(Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("id")))).get(pos).getId()));
            intent.putExtra("d2",pos);
            startActivity(intent);
        }
        return super.onContextItemSelected(item);

    }

    @Override
    protected void onResume() {

        db=Appdatabase.getDatabase(getApplicationContext());

        adapter = new LectureAdapter(getApplicationContext(),db.lectureDao().getAllLecturesByCourseID(
                Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("id")))) , new LectureAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(AddEditCorseActivity.this, "Clicked position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongItemClick(int position) {
                pos = position;
                registerForContextMenu(binding.recycleLectures);
            }
        });
        binding.recycleLectures.setAdapter(adapter); // ربط RecyclerView مع الـ Adapter
        binding.recycleLectures.setLayoutManager(new LinearLayoutManager(AddEditCorseActivity.this));


        super.onResume();

    }
}