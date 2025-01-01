package com.example.coursesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
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
import androidx.room.Room;

import com.example.coursesapp.databinding.ActivitySignUpBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    Appdatabase db;
    boolean flag;
    Uri imageUri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db=Appdatabase.getDatabase(this);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ActivityResultLauncher<Intent> lancher2=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent =result.getData();
                assert intent != null;
                imageUri= intent.getData();
                binding.imageUserSign.setImageURI(imageUri);


            }
        }); {
        }


        //ينقلك للمعرض وبعدها يستخدم lancher2 ويعرض الصورة
        binding.cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                Intent intent2=new Intent();
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                lancher2.launch(intent2);
            }
        });










        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (binding.edUsername.getText().toString().isEmpty() || binding.edEmail.getText().toString().isEmpty() || binding.edPassword.getText().toString().isEmpty() || binding.edRepassword.getText().toString().isEmpty()||binding.edPhone.getText().toString().isEmpty()){
                        Toast.makeText(SignUpActivity.this, "Enter Data", Toast.LENGTH_SHORT).show();
                    }else if (!binding.edPassword.getText().toString().equals(binding.edRepassword.getText().toString())){
                        Toast.makeText(SignUpActivity.this, "Password Not The Same", Toast.LENGTH_SHORT).show();
                    }else {
                        if (db.userDao().getUserByEmail(binding.edEmail.getText().toString())!=null){
                            Toast.makeText(SignUpActivity.this, "Email Used Try Another", Toast.LENGTH_SHORT).show();
                        } else if (!binding.edEmail.getText().toString().contains("@")) {
                            binding.edEmail.setError("Invalid Email");
                            binding.edEmail.requestFocus();
                            
                        } else {
                            User user=new User(binding.edUsername.getText().toString(),binding.edEmail.getText().toString(),binding.edPassword.getText().toString());
                            user.setJoinDate(getFormattedDate());
                            user.setPhoneNumber(Integer.parseInt(binding.edPhone.getText().toString()));

                            if (flag) {


                                File externalStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "coursesapp");
                                if (!externalStorageDirectory.exists()) {
                                    externalStorageDirectory.mkdirs();
                                }
                                String uniqueName = UUID.randomUUID().toString();

                                File file = new File(externalStorageDirectory, "user_" + user.getId() + "_" +uniqueName +"_"+getFormattedDateForFilename() + ".jpg");
                                try {
                                    saveImageToStorage(Objects.requireNonNull(uriToBitmap(imageUri, SignUpActivity.this)), file.getAbsolutePath());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }


                                user.setUserImagePath(file.getAbsolutePath());
                                loadImageFromStorage(user.getUserImagePath(), binding.imageUserSign);
                            }else {
                                user.setUserImagePath("null");
                            }

                            db.userDao().insertUser(user);
                            Toast.makeText(SignUpActivity.this, "User Added", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
            }
        });




    }
    public static String getFormattedDate() {
        // إنشاء كائن تاريخ يحتوي على الوقت الحالي
        Date currentDate = new Date();

        // التنسيق المطلوب: اليوم رقم، الشهر كلمة، السنة
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", new Locale("en"));

        // تحويل التاريخ إلى النص المطلوب
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


    public String saveImageToStorage(Bitmap bitmap, Context context) {
        String fileName = "user_" + System.currentTimeMillis() + ".png";
        File file = new File(context.getFilesDir(), fileName);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath(); // إرجاع المسار الكامل للصورة
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





}