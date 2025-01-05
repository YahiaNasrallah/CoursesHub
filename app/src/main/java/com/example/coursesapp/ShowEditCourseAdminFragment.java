package com.example.coursesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.coursesapp.databinding.FragmentShowEditCourseAdminBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowEditCourseAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowEditCourseAdminFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentShowEditCourseAdminBinding binding;
    Uri imageUri=null;
    String LectureNum;
    int LecNum;
    String Category;

    boolean flag3=false;
    boolean flag=false;

    Appdatabase db;
    private long courseid;


    public ShowEditCourseAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowEditCourseAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowEditCourseAdminFragment newInstance(String param1, String param2) {
        ShowEditCourseAdminFragment fragment = new ShowEditCourseAdminFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentShowEditCourseAdminBinding.inflate(inflater,container,false);
         db=Appdatabase.getDatabase(getContext());





        Course courseSHow = db.courseDao().getCoursesByID(courseid);
        binding.edTitle.setText(courseSHow.getTitle());
        binding.edDetails.setText(courseSHow.getDetails());
        binding.edPrice.setText(courseSHow.getPrice());
        binding.edHourse.setText(courseSHow.getHours());
        binding.edInsName.setText(courseSHow.getInstructorName());
        binding.edDescription.setText(courseSHow.getDescription());
        binding.edCategorynameshow.setText(courseSHow.getCategorynameshown());
        loadImageFromStorage(courseSHow.getImagePath(), binding.imageCourse);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
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



        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(requireContext(),
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
            binding.edCategorynameshow.setText("Education");
            Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
            binding.categorySpinner.setSelection(0);
        } else if (db.categoryDao().getCategoryById(courseSHow.getCategoryID()).getCategoryName().equals("Engineering")) {
            binding.edCategorynameshow.setText("Engineering");
            binding.categorySpinner.setSelection(1);
            Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
        } else if (db.categoryDao().getCategoryById(courseSHow.getCategoryID()).getCategoryName().equals("Business")) {
            binding.edCategorynameshow.setText("Business");
            binding.categorySpinner.setSelection(2);
            Toast.makeText(getContext(), "3", Toast.LENGTH_SHORT).show();
        } else {
            binding.edCategorynameshow.setText("Other");
            binding.categorySpinner.setSelection(3);
            Toast.makeText(getContext(), "4", Toast.LENGTH_SHORT).show();
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
                    course.setId(courseid);
                    course.setCategorynameshown(binding.edCategorynameshow.getText().toString());

                    if (flag3){



                        File externalStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "coursesapp");
                        if (!externalStorageDirectory.exists()) {
                            externalStorageDirectory.mkdirs();
                            File noMediaFile = new File(externalStorageDirectory, ".nomedia");
                            try {
                                noMediaFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        String uniqueName = UUID.randomUUID().toString();

                        File file2 = new File(externalStorageDirectory, "course_" +uniqueName +"_"+getFormattedDateForFilename() + ".jpg");
                        try {
                            saveImageToStorage(Objects.requireNonNull(uriToBitmap(imageUri, requireContext())), file2.getAbsolutePath());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }



                        course.setImagePath(file2.getAbsolutePath());
                        loadImageFromStorage(course.getImagePath(), binding.imageCourse);

                        loadImageFromStorage(course.getImagePath(), binding.imageCourse);
                        File fileToDelete = new File(courseSHow.getImagePath());
                        if(fileToDelete.exists()) {
                            Toast.makeText(getContext(), "deletd", Toast.LENGTH_SHORT).show();
                            fileToDelete.delete();
                        }
                    }else {
                        course.setImagePath(courseSHow.getImagePath());

                    }


                    db.courseDao().updateCourse(course);
                    Toast.makeText(getContext(), "Course Updated", Toast.LENGTH_SHORT).show();
                    requireActivity().finish();


                }
            }
        });




        //-----------------------Gallery-------------------------------------------

        ActivityResultLauncher<Intent> lancher2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) { // تحقق من أن النتيجة ناجحة
                            Intent intent = result.getData();
                            if (intent != null && intent.getData() != null) { // تحقق من أن البيانات ليست فارغة
                                imageUri = intent.getData();
                                binding.imageCourse.setImageURI(imageUri); // عرض الصورة
                            } else {
                                Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

// فتح المعرض عند الضغط
        binding.cardImageCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag3 = true;
                flag = true;
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                lancher2.launch(intent2);
            }
        });







        return binding.getRoot();
    }

    public void loadImageFromStorage(String imagePath, ImageView imageView) {
        File imgFile = new  File(imagePath);
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
    }
    public void saveImageToStorage(Bitmap bitmap, String imagePath) throws IOException {
        File file = new File(imagePath);  // استخدم المسار الذي تريد تخزين الصورة فيه
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
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


    public void updateData(long courseid) {
        this.courseid = courseid;




    }


}