package com.example.coursesapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coursesapp.databinding.ActivityAddLectureBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddLectureActivity extends AppCompatActivity {

    ActivityAddLectureBinding binding;
    Appdatabase db;
    String selectedCourse;
    String selectedLecture;
    int selectedLectureint;
    List<String> integers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddLectureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = Appdatabase.getDatabase(this);

//
//        if (getIntent().getStringExtra("zz").equals("edit")) {
//            Lecture lecture=db.lectureDao().getLectureByID(Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("d1"))));
//            binding.edLectureName.setText(lecture.getLectureName());
//            binding.edLectureLink.setText(lecture.getLectureLink());
//            binding.edLectureDescription.setText(lecture.getDescription());
//
//
//
//           // Toast.makeText(this, String.valueOf(db.courseDao().getCoursesByID(lecture.getCourseID()).getLectureNumber()), Toast.LENGTH_SHORT).show();
////            for (int i = 0; i <integers.size() ; i++) {
////                if (integers.get(i).equals(String.valueOf(db.courseDao().getCoursesByID(lecture.getCourseID()).getLectureNumber()))){
////                    binding.lecturenumSpinner.setSelection(i);
////
////                }
////            }
//
//
//            binding.btnSave.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (binding.edLectureName.getText().toString().isEmpty()) {
//                        binding.edLectureName.setError("Enter Lecture Name");
//                        binding.edLectureName.requestFocus();
//                    }else if (binding.edLectureLink.getText().toString().isEmpty()) {
//                        binding.edLectureLink.setError("Enter Lecture Link");
//                        binding.edLectureLink.requestFocus();
//
//                    }else if (binding.edLectureDescription.getText().toString().isEmpty()) {
//                        binding.edLectureDescription.setError("Enter Lecture Description");
//                        binding.edLectureDescription.requestFocus();
//                    }else {
//                        if (selectedCourse==null){
//                            Toast.makeText(AddLectureActivity.this, "Choose Course", Toast.LENGTH_SHORT).show();
//                        } else if (selectedLecture==null) {
//                            Toast.makeText(AddLectureActivity.this, "Choose Lecture Number", Toast.LENGTH_SHORT).show();
//                        } else if (binding.edLectureName.getText().toString().isEmpty()) {
//                            binding.edLectureName.setError("Enter Lecture Name");
//                            binding.edLectureName.requestFocus();
//                        }else if (binding.edLectureLink.getText().toString().isEmpty()) {
//                            binding.edLectureLink.setError("Enter Lecture Link");
//                            binding.edLectureLink.requestFocus();
//                        }else if (binding.edLectureDescription.getText().toString().isEmpty()) {
//                            binding.edLectureDescription.setError("Enter Lecture Description");
//                            binding.edLectureDescription.requestFocus();
//                        } else {
//
////                        for (int i = 0; i <db.lectureDao().getAllLecturesByCourseID(db.courseDao().getCourseByTitle(selectedCourse).getId()).size() ; i++) {
////                            if (db.lectureDao().getAllLecturesByCourseID(db.courseDao().getCourseByTitle(selectedCourse).getId()).get(i).getLectureNumber()==selectedLectureint) {
////                                Toast.makeText(AddLectureActivity.this, "Lecture Already Exists", Toast.LENGTH_SHORT).show();
////
////                            }else {
////                                Lecture lecture=new Lecture();
////                                lecture.setCourseID(db.courseDao().getCourseByTitle(selectedCourse).getId());
////                                lecture.setLectureNumber(selectedLectureint);
////                                lecture.setLectureName(binding.edLectureName.getText().toString());
////                                lecture.setLectureLink(binding.edLectureLink.getText().toString());
////                                lecture.setDescription(binding.edLectureDescription.getText().toString());
////
////
////                                db.lectureDao().insertLecture(lecture);
////                                Toast.makeText(AddLectureActivity.this, "Lecture Added", Toast.LENGTH_SHORT).show();
////                                finish();
////                            }
////
////                        }
//                            List<Lecture> lectures = db.lectureDao().getAllLecturesByCourseID(
//                                    db.courseDao().getCourseByTitle(selectedCourse).getId()
//                            );
//
//                            boolean lectureExists = false;
//
//                            for (Lecture lecture : lectures) {
//                                if (lecture.getLectureNumber() == selectedLectureint) {
//                                    lectureExists = true;
//                                    break;
//                                }
//                            }
//
//
//                                Lecture lecture = new Lecture();
//                                lecture.setId(db.lectureDao().getLectureByID(Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("d1")))).getId());
//                                lecture.setCourseID(db.courseDao().getCourseByTitle(selectedCourse).getId());
//                                lecture.setLectureNumber(selectedLectureint);
//                                lecture.setLectureName(binding.edLectureName.getText().toString());
//                                lecture.setLectureLink(binding.edLectureLink.getText().toString());
//                                lecture.setDescription(binding.edLectureDescription.getText().toString());
//
//
//                                db.lectureDao().updateLecture(lecture);
//                                Toast.makeText(AddLectureActivity.this, "Lecture Updated", Toast.LENGTH_SHORT).show();
//                                finish();
//
//                        }
//
//
//                    }
//
//                }
//            });
//
//
//        }
//
//
//
//


    }
}