package com.example.coursesapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.databinding.CategoryViewItemBinding;
import com.example.coursesapp.databinding.MycoursesItemBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MycoursesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MyCourses> myCoursesList;
    long savedid;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //استدعاء الinterface
    private MycoursesAdapter.ClickHandle clickHandle;

    MycoursesItemBinding binding;


    public MycoursesAdapter(Context context, List<MyCourses> myCoursesList, MycoursesAdapter.ClickHandle clickHandle) {
        this.context = context;
        this.myCoursesList = myCoursesList;
        this.clickHandle=clickHandle;
    }
    public void updateCourses(List<MyCourses> newCourses) {
        this.myCoursesList = newCourses;
        notifyDataSetChanged(); // تحديث واجهة RecyclerView
    }

    //الدالتين لاعطاء الاوامر للعناصر
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=MycoursesItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MycoursesAdapter.MyviewHolder(binding);
    }

    //الوصول لكل عنصر محدد من الليست
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Appdatabase db=Appdatabase.getDatabase(context);
        MycoursesAdapter.MyviewHolder myviewHolder= (MycoursesAdapter.MyviewHolder) holder;

        preferences = context.getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();
        savedid  = preferences.getLong("savedid", 0);

        myviewHolder.binding.tvCoursename.setText(db.courseDao().getCoursesByID(myCoursesList.get(position).getCourseID()).getTitle());
        myviewHolder.binding.tvHourse.setText("Hourse: "+db.courseDao().getCoursesByID(myCoursesList.get(position).getCourseID()).getHours());
        myviewHolder.binding.tvLecnum.setText("Lecture Numbers: "+db.courseDao().getCoursesByID(myCoursesList.get(position).getCourseID()).getLectureNumber());
        myviewHolder.binding.progressBar.setProgress(myCoursesList.get(position).getProgress());



        // تحقق إذا كانت الدورة مكتملة
        if (myCoursesList.get(position).isCompleted()) {
            // إخفاء العناصر
            myviewHolder.binding.tvHourse.setVisibility(View.GONE);
            myviewHolder.binding.tvLecnum.setVisibility(View.GONE);
            myviewHolder.binding.btnCertificte.setVisibility(View.VISIBLE);
            myviewHolder.binding.linProgres.setVisibility(View.GONE);


            myviewHolder.binding.btnCertificte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Button clicked!"); // طباعة رسالة للتحقق

                    // تحديد مسار الحفظ في مجلد الصور العام
                    File externalStorageDirectory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "coursesapp");
                    if (!externalStorageDirectory.exists()) {
                        externalStorageDirectory.mkdirs(); // إنشاء المجلد إذا لم يكن موجودًا
                    }

                    // إنشاء اسم فريد للملف
                    String uniqueName = UUID.randomUUID().toString();
                    File file = new File(externalStorageDirectory, "cert_" + savedid + "_" + uniqueName + "_" + getFormattedDateForFilename() + ".pdf");

                    // إنشاء كائن CertificateGenerator واستدعاء الدالة لإنشاء الشهادة
                    CertificateGenerator generator = new CertificateGenerator();
                    generator.generateCertificate(
                            db.userDao().getUserByid(savedid).getUsername(),
                            db.courseDao().getCoursesByID(myCoursesList.get(position).getCourseID()).getTitle(),
                            file.getAbsolutePath()
                    );

                    // فتح ملف PDF باستخدام FileProvider
                    Uri pdfUri = androidx.core.content.FileProvider.getUriForFile(
                            context,
                            context.getPackageName() + ".provider",
                            file
                    );

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "No PDF viewer available", Toast.LENGTH_LONG).show();
                    }

                }
            });



            // عرض تاريخ الإكمال
            myviewHolder.binding.tvCompleteDate.setVisibility(View.VISIBLE);
            myviewHolder.binding.tvCompleteDate.setText("Completed on: " + myCoursesList.get(position).getCompleteDate());
        } else {
            // إذا لم تكن مكتملة، أظهر العناصر وأخفي تاريخ الإكمال
            myviewHolder.binding.tvHourse.setVisibility(View.VISIBLE);
            myviewHolder.binding.tvLecnum.setVisibility(View.VISIBLE);
            myviewHolder.binding.tvCompleteDate.setVisibility(View.GONE);

            myviewHolder.binding.btnCertificte.setVisibility(View.GONE);
        }



        myviewHolder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandle.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myCoursesList.size();
    }


    //ربط التصميم
    public class MyviewHolder extends RecyclerView.ViewHolder{

        MycoursesItemBinding binding;
        public MyviewHolder(MycoursesItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }



    //اصدار امر عند الضغط على عنصر ولكن من main
    public interface ClickHandle{
        void onItemClick(int position);

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
