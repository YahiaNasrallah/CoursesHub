package com.example.coursesapp;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

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
    boolean flag2=false;
    Uri imageUri;

    boolean flag3=false;


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
                    if (Category.equals("Other")){
                        binding.edCategorynameshow.getText().clear();
                        binding.edCategorynameshow.setVisibility(View.VISIBLE);
                    }else {
                        binding.edCategorynameshow.setVisibility(View.GONE);
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

                        for (int i = 0; i <db.courseDao().getAllCourses().size() ; i++) {
                            if (db.courseDao().getAllCourses().get(i).getTitle().equalsIgnoreCase(binding.edTitle.getText().toString())){
                                flag2=true;
                            }

                        }

                        if (flag2){
                            flag2=false;
                            binding.edTitle.setError("Title Already Exists");
                            binding.edTitle.requestFocus();
                        }else {
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

                            File externalStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "coursesapp");
                            if (!externalStorageDirectory.exists()) {
                                externalStorageDirectory.mkdirs();
                            }
                            String uniqueName = UUID.randomUUID().toString();

                            File file = new File(externalStorageDirectory, "course_"  +uniqueName +"_"+getFormattedDateForFilename() + ".jpg");
                            try {
                                saveImageToStorage(Objects.requireNonNull(uriToBitmap(imageUri, AddEditCorseActivity.this)), file.getAbsolutePath());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }


                            course.setImagePath(file.getAbsolutePath());

                            db.courseDao().insertCourse(course);
                            Toast.makeText(AddEditCorseActivity.this, "Course Added", Toast.LENGTH_SHORT).show();
                            Toast.makeText(AddEditCorseActivity.this, Category, Toast.LENGTH_SHORT).show();
                            finish();

                        }


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


            Course courseSHow = db.courseDao().getCoursesByID(Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("id"))));
            binding.edTitle.setText(courseSHow.getTitle());
            binding.edDetails.setText(courseSHow.getDetails());
            binding.edPrice.setText(courseSHow.getPrice());
            binding.edHourse.setText(courseSHow.getHours());
            binding.edInsName.setText(courseSHow.getInstructorName());
            binding.edDescription.setText(courseSHow.getDescription());
            binding.edCategorynameshow.setText(courseSHow.getCategorynameshown());
            loadImageFromStorage(courseSHow.getImagePath(), binding.imageCourse);


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


            binding.lecturenumSpinner.setSelection(courseSHow.getLectureNumber() - 1);
            if (db.categoryDao().getCategoryById(courseSHow.getCategoryID()).getCategoryName().equals("Education")) {
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                binding.categorySpinner.setSelection(0);
            } else if (db.categoryDao().getCategoryById(courseSHow.getCategoryID()).getCategoryName().equals("Engineering")) {
                binding.categorySpinner.setSelection(1);
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
            } else if (db.categoryDao().getCategoryById(courseSHow.getCategoryID()).getCategoryName().equals("Business")) {
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
                    } else if (binding.edCategorynameshow.getText().toString().isEmpty()) {
                        binding.edCategorynameshow.setError("Enter Category Name");
                        binding.edCategorynameshow.requestFocus();

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
                        course.setCategorynameshown(binding.edCategorynameshow.getText().toString());

                        if (flag3){



                            File externalStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "coursesapp");
                            if (!externalStorageDirectory.exists()) {
                                externalStorageDirectory.mkdirs();
                            }
                            String uniqueName = UUID.randomUUID().toString();

                            File file2 = new File(externalStorageDirectory, "course_" +uniqueName +"_"+getFormattedDateForFilename() + ".jpg");
                            try {
                                saveImageToStorage(Objects.requireNonNull(uriToBitmap(imageUri, AddEditCorseActivity.this)), file2.getAbsolutePath());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }



                            course.setImagePath(file2.getAbsolutePath());
                            loadImageFromStorage(course.getImagePath(), binding.imageCourse);

                            loadImageFromStorage(course.getImagePath(), binding.imageCourse);
                            File fileToDelete = new File(courseSHow.getImagePath());
                            if(fileToDelete.exists()) {
                                Toast.makeText(AddEditCorseActivity.this, "deletd", Toast.LENGTH_SHORT).show();
                                fileToDelete.delete();
                            }
                        }else {
                            course.setImagePath(courseSHow.getImagePath());

                        }


                        db.courseDao().updateCourse(course);
                        Toast.makeText(AddEditCorseActivity.this, "Course Updated", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }
            });
        }













        //-----------------------Gallery-------------------------------------------

        ActivityResultLauncher<Intent> lancher2=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent =result.getData();
                assert intent != null;
                imageUri= intent.getData();
                binding.imageCourse.setImageURI(imageUri);


            }
        }); {
        }


        //ينقلك للمعرض وبعدها يستخدم lancher2 ويعرض الصورة
        binding.cardImageCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag3=true;
                flag = true;
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                lancher2.launch(intent2);
            }
        });








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

       if (Objects.equals(getIntent().getStringExtra("zz"), "edit")){

           adapter = new LectureAdapter(getApplicationContext()
                   ,db.lectureDao().getAllLecturesByCourseID( Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("id")))), new LectureAdapter.ClickHandle() {
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


       }

        super.onResume();

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

}