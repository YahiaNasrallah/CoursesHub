package com.example.coursesapp;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coursesapp.databinding.ActivityLoginBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {


    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Appdatabase db;
    ActivityLoginBinding binding;

    String AdminEmail="a";
    String AdminPassword="a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = Appdatabase.getDatabase(this);
        preferences = getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();




        View coustem= LayoutInflater.from(LoginActivity.this).inflate(R.layout.user_not_found_item,null);
        AlertDialog.Builder bulder=new AlertDialog.Builder(this);
        bulder.setView(coustem);
        AlertDialog dialog=bulder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        if (db.categoryDao().getAllCategory().isEmpty()){
            db.categoryDao().insertCategory(new Category("Education"));
            db.categoryDao().insertCategory(new Category("Engineering"));
            db.categoryDao().insertCategory(new Category("Business"));
            db.categoryDao().insertCategory(new Category("Other"));

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

            File file = new File(externalStorageDirectory, "course_" +uniqueName +"_"+getFormattedDateForFilename() + ".jpg");

            try {
                saveImageToStorage(intToBitmap(R.drawable.img,LoginActivity.this), file.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Course course=new Course("Android Devlopment","Android Studio","eng.yahia","90",0,"13","s simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting",3,db.categoryDao().getCategoryByTitle("Other").getId(),"Programming");
            course.setImagePath(file.getAbsolutePath());
            db.courseDao().insertCourse(course);
            User user=new User("Yahia","yahianasrallah2004@gmail.com","a");
            user.setPhoneNumber(595282048);
            user.setLastName("Hassan");
            user.setJoinDate("1 Jan 2025");
                    user.setUserImagePath(file.getAbsolutePath());
                    db.userDao().insertUser(user);






        }



        if (preferences.contains("savedName")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {




            binding.btnSignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (binding.edEmail.getText().toString().isEmpty() || binding.edPassword.getText().toString().isEmpty()) {
                        if (binding.edEmail.getText().toString().isEmpty()) {
                            binding.edEmail.setError("Enter Email");
                            binding.edEmail.requestFocus();
                        }
                        if (binding.edPassword.getText().toString().isEmpty()) {
                            binding.edPassword.setError("Enter Password");
                            binding.edPassword.requestFocus();
                        }
                        //Toast.makeText(LoginActivity.this, "Enter Data", Toast.LENGTH_SHORT).show();
                    } else  if (binding.edPassword.getText().toString().equals(AdminPassword)&&binding.edEmail.getText().toString().equals(AdminEmail)) {
                        binding.edEmail.getText().clear();
                        binding.edPassword.getText().clear();
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));

                    } else {

                             User user = db.userDao().getUser(binding.edEmail.getText().toString(), binding.edPassword.getText().toString());
                             User user2 = db.userDao().getUserByEmail(binding.edEmail.getText().toString());
                        if (user2 == null) {


                            dialog.show();
                            coustem.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.cancel();
                                }
                            });

                        } else if (user==null) {
                            Toast.makeText(LoginActivity.this, "Email Or Password Incorrect", Toast.LENGTH_SHORT).show();


                        } else if (db.userDao().getUser(binding.edEmail.getText().toString(), binding.edPassword.getText().toString()) != null) {
                            if (binding.checkbox.isChecked()){
                                editor.putString("savedName",binding.edEmail.getText().toString());
                                editor.putString("savedPAssword",binding.edPassword.getText().toString());
                                editor.commit();

                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            long id=db.userDao().getUser(binding.edEmail.getText().toString(), binding.edPassword.getText().toString()).getId();
                            editor.putLong("savedid",user.getId());
                            editor.commit();

                            intent.putExtra("id", id);
                            for (int i = 0; i <db.notificationDao().getAllNotifications(user.getId()).size() ; i++) {
                                if (!db.notificationDao().getAllNotifications(user.getId()).get(i).isNotified()){
                                    Notification notification=db.notificationDao().getAllNotifications(user.getId()).get(i);
                                    notification.setNotified(true);
                                    db.notificationDao().updateNotification(notification);
                                    showNotification(LoginActivity.this,notification);
                                }

                            }

                            startActivity(intent);


                            finish();
                            binding.edEmail.getText().clear();
                            binding.edPassword.getText().clear();
                        }
                    }
                }


            });

            binding.btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.edEmail.getText().clear();
                    binding.edPassword.getText().clear();
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                }
            });


        }
    }
    public void saveImageToStorage(Bitmap bitmap, String imagePath) throws IOException {
        File file = new File(imagePath);  // استخدم المسار الذي تريد تخزين الصورة فيه
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
    }
    public Bitmap intToBitmap(int drawableId, Context context) {
        Drawable drawable = context.getDrawable(drawableId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public static String getFormattedDateForFilename() {
        // إنشاء كائن تاريخ يحتوي على الوقت الحالي
        Date currentDate = new Date();

        // التنسيق المطلوب: اليوم-الشهر-السنة_الساعة-الدقيقة-الثانية
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

        // تحويل التاريخ إلى نص بدون رموز أو مسافات
        return dateFormat.format(currentDate);
    }



    public static void showNotification(Context context, Notification notification) {
        String channelId = "new_default_channel_id";
        String channelName = "NewDefaultChannel";
        Intent intent = new Intent(context, CourseDetailsActivity.class);
        intent.putExtra("course_id",notification.getCoourseID());
        intent.putExtra("from", "mycourses"); // تمرير بيانات إضافية إذا لزم الأمر
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_logo_round) // تأكد من أن الأيقونة صحيحة
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getMessage())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent) // ربط الـ PendingIntent بالإشعار
                .setAutoCancel(true);
        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }

}