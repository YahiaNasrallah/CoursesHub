package com.example.coursesapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.databinding.MycoursesItemBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MycoursesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MyCourses> myCoursesList;
    private long savedid;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private MycoursesAdapter.ClickHandle clickHandle;
    private MycoursesItemBinding binding;

    public MycoursesAdapter(Context context, List<MyCourses> myCoursesList, MycoursesAdapter.ClickHandle clickHandle) {
        this.context = context;
        this.myCoursesList = myCoursesList;
        this.clickHandle = clickHandle;
    }

    public void updateCourses(List<MyCourses> newCourses) {
        this.myCoursesList = newCourses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = MycoursesItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyviewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Appdatabase db = Appdatabase.getDatabase(context);
        MyviewHolder myviewHolder = (MyviewHolder) holder;

        preferences = context.getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();
        savedid = preferences.getLong("savedid", 0);

        // استرجاع بيانات الدورة
        MyCourses course = myCoursesList.get(position);

        myviewHolder.binding.tvCoursename.setText(db.courseDao().getCoursesByID(course.getCourseID()).getTitle());
        myviewHolder.binding.tvHourse.setText("Hourse: " + db.courseDao().getCoursesByID(course.getCourseID()).getHours());
        myviewHolder.binding.tvLecnum.setText("Lecture Numbers: " + db.courseDao().getCoursesByID(course.getCourseID()).getLectureNumber());
        myviewHolder.binding.progressBar.setProgress(course.getProgress());

        if (course.isCompleted()) {
            myviewHolder.binding.tvHourse.setVisibility(View.GONE);
            myviewHolder.binding.tvLecnum.setVisibility(View.GONE);
            myviewHolder.binding.btnCertificte.setVisibility(View.VISIBLE);
            myviewHolder.binding.linProgres.setVisibility(View.GONE);

            myviewHolder.binding.btnCertificte.setOnClickListener(view -> {
                System.out.println("Button clicked!");

                // حفظ الصورة في SharedPreferences
                String logoPath = preferences.getString("logo_path", null);
                if (logoPath == null) {
                    Drawable logoDrawable = context.getDrawable(R.drawable.logo); // الصورة من drawable
                    Bitmap logoBitmap = ((BitmapDrawable) logoDrawable).getBitmap();
                    File logoFile = new File(context.getCacheDir(), "app_logo.png");
                    saveBitmapToFile(logoBitmap, logoFile);
                    logoPath = logoFile.getAbsolutePath();
                    editor.putString("logo_path", logoPath);
                    editor.apply();
                }

                // إنشاء ملف الشهادة
                File externalStorageDirectory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "coursesapp");
                if (!externalStorageDirectory.exists()) {
                    externalStorageDirectory.mkdirs();
                }

                String uniqueName = UUID.randomUUID().toString();
                File file = new File(externalStorageDirectory, "cert_" + savedid + "_" + uniqueName + ".pdf");

                try (OutputStream outputStream = new FileOutputStream(file)) {
                    CertificateGenerator.generateCertificate(
                            db.userDao().getUserByid(savedid).getFirstName()+" "+db.userDao().getUserByid(savedid).getLastName(),
                            db.courseDao().getCoursesByID(course.getCourseID()).getTitle(),
                            Integer.parseInt(db.courseDao().getCoursesByID(course.getCourseID()).getHours()),
                            outputStream,
                            logoPath
                    );

                    Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    context.startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed to generate certificate", Toast.LENGTH_LONG).show();
                }
            });

            myviewHolder.binding.tvCompleteDate.setVisibility(View.VISIBLE);
            myviewHolder.binding.tvCompleteDate.setText("Completed on: " + course.getCompleteDate());

        }
        else {

            myviewHolder.binding.tvHourse.setVisibility(View.VISIBLE);
            myviewHolder.binding.tvLecnum.setVisibility(View.VISIBLE);
            myviewHolder.binding.tvCompleteDate.setVisibility(View.GONE);
            myviewHolder.binding.btnCertificte.setVisibility(View.GONE);
        }

        myviewHolder.binding.getRoot().setOnClickListener(view -> clickHandle.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return myCoursesList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        MycoursesItemBinding binding;

        public MyviewHolder(MycoursesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static String getFormattedDateForFilename() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    public void saveBitmapToFile(Bitmap bitmap, File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ClickHandle {
        void onItemClick(int position);
    }
}
