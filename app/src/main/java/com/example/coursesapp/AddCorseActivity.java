package com.example.coursesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coursesapp.databinding.ActivityAddCorseBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class AddCorseActivity extends AppCompatActivity {

    ActivityAddCorseBinding binding;
    Appdatabase db;
    String Category;
    ArrayList<User> TempDeletedUser;
    int pos;
    boolean flag=false;
    LectureAdapter adapter;
    boolean flag2=false;
    Uri imageUri=null;
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
                if (Category.equals("Other")) {
                    binding.edCategorynameshow.getText().clear();
                    binding.containerEdShowcategory.setVisibility(View.VISIBLE);
                } else {
                    binding.containerEdShowcategory.setVisibility(View.GONE);
                    binding.edCategorynameshow.setText(Category);
                }
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
                } else if (binding.edCategorynameshow.getText().toString().isEmpty()) {
                    binding.edCategorynameshow.setError("Enter Category Name");
                    binding.edCategorynameshow.requestFocus();

                } else {

                    for (int i = 0; i < db.courseDao().getAllCourses().size(); i++) {
                        if (db.courseDao().getAllCourses().get(i).getTitle().equalsIgnoreCase(binding.edTitle.getText().toString())) {
                            flag2 = true;
                        }

                    }

                    if (flag2) {
                        flag2 = false;
                        binding.edTitle.setError("Title Already Exists");
                        binding.edTitle.requestFocus();
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
                        course.setCategorynameshown(binding.edCategorynameshow.getText().toString());
                        course.setCategoryID(db.categoryDao().getCategoryByTitle(Category).getId());


                        if (flag) {
                            File externalStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "coursesapp");
                            if (!externalStorageDirectory.exists()) {
                                externalStorageDirectory.mkdirs();
                            }
                            String uniqueName = UUID.randomUUID().toString();

                            File file = new File(externalStorageDirectory, "course_" + uniqueName + "_" + getFormattedDateForFilename() + ".jpg");
                            try {
                                saveImageToStorage(Objects.requireNonNull(uriToBitmap(imageUri, AddCorseActivity.this)), file.getAbsolutePath());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            course.setImagePath(file.getAbsolutePath());
                        }else {
                            course.setImagePath("null");
                        }
                        db.courseDao().insertCourse(course);

                        Toast.makeText(AddCorseActivity.this, "Course Added", Toast.LENGTH_SHORT).show();
                        Toast.makeText(AddCorseActivity.this, Category, Toast.LENGTH_SHORT).show();
                        finish();

                    }


                }
            }
        });



        //-----------------------Gallery-------------------------------------------

        ActivityResultLauncher<Intent> lancher2=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) { // تحقق من أن النتيجة ناجحة
                    Intent intent = result.getData();
                    if (intent != null && intent.getData() != null) { // تحقق من أن البيانات ليست فارغة
                        imageUri = intent.getData();
                        binding.imageCourse.setImageURI(imageUri); // عرض الصورة
                    } else {
                        Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }); {
        }


        //ينقلك للمعرض وبعدها يستخدم lancher2 ويعرض الصورة
        binding.cardImageCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                lancher2.launch(intent2);
            }
        });




        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
















    }




    public void saveImageToStorage(Bitmap bitmap, String imagePath) throws IOException {
        File file = new File(imagePath);  // استخدم المسار الذي تريد تخزين الصورة فيه
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
    }


    public void loadImageFromStorage(String imagePath, ImageView imageView) {
        File imgFile = new  File(imagePath);
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
    }


    public static String getFormattedDateForFilename() {
        // إنشاء كائن تاريخ يحتوي على الوقت الحالي
        Date currentDate = new Date();

        // التنسيق المطلوب: اليوم-الشهر-السنة_الساعة-الدقيقة-الثانية
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

        // تحويل التاريخ إلى نص بدون رموز أو مسافات
        return dateFormat.format(currentDate);
    }



    private Bitmap uriToBitmap(Uri uri, Context context) {
        try {
            // استخدم ContentResolver لتحميل الصورة كـ Bitmap
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // إذا حدث خطأ
        }
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}