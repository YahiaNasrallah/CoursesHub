package com.example.coursesapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.coursesapp.databinding.FragmentProfileBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentProfileBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    long savedid;
    Uri imageUri=null;
    boolean flag;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        preferences = requireContext().getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();
        savedid  = preferences.getLong("savedid", 0);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProfileBinding.inflate(inflater,container,false);

        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                Intent intent=new Intent(getContext(),LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        Appdatabase db=Appdatabase.getDatabase(getContext());
        User user=db.userDao().getUserByid(savedid);
        int completed=db.myCoursesDao().getAllMyCourses(savedid,true).size();
        int ongoing=db.myCoursesDao().getAllMyCourses(savedid,false).size();
        binding.tvEmail.setText(user.getEmail());
        binding.tvPhone.setText(String.valueOf(user.getPhoneNumber()));
        binding.tvJoinedin.setText("Joined on "+user.getJoinDate());
        binding.tvUserName.setText(user.getFirstName().toUpperCase());
        binding.tvCompletes.setText(completed+" Completed");
        binding.tvOngoing.setText(ongoing+" Ongoing");
        binding.edLastname.setText(user.getLastName());
        loadImageFromStorage(user.getUserImagePath(),binding.imageUserProfile);



        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.linShowdata.setVisibility(View.GONE);
                binding.linEditdata.setVisibility(View.VISIBLE);

                binding.edEmail.setText(user.getEmail());
                binding.edPhone.setText(String.valueOf(user.getPhoneNumber()));
                binding.edFirstname.setText(user.getFirstName());
                binding.edLastname.setText(user.getLastName());
                binding.edPassword.setText(user.getPassword());
                binding.edRepassword.setText(user.getPassword());
                loadImageFromStorage(user.getUserImagePath(),binding.imageUserEdit);
            }
        });

        binding.btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldpath=user.getUserImagePath();
                user.setId(savedid);
                user.setFirstName(binding.edFirstname.getText().toString());
                user.setLastName(binding.edLastname.getText().toString());
                user.setEmail(binding.edEmail.getText().toString());
                user.setPhoneNumber(Integer.parseInt(binding.edPhone.getText().toString()));
                user.setPassword(binding.edPassword.getText().toString());
                user.setJoinDate(user.getJoinDate());

                if (flag){

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

                    File file = new File(externalStorageDirectory, "user_" + savedid + "_" +uniqueName +"_"+getFormattedDateForFilename() + ".jpg");
                    try {
                        saveImageToStorage(Objects.requireNonNull(uriToBitmap(imageUri,requireContext())), file.getAbsolutePath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    user.setUserImagePath(file.getAbsolutePath());
                    loadImageFromStorage(user.getUserImagePath(), binding.imageUserEdit);
                    File fileToDelete = new File(oldpath);
                    if(fileToDelete.exists()) {
                        Toast.makeText(getContext(), "deletd", Toast.LENGTH_SHORT).show();
                        fileToDelete.delete();
                    }


                }else {
                    user.setUserImagePath(user.getUserImagePath());
                }



                db.userDao().updateUser(user);
                binding.linEditdata.setVisibility(View.GONE);
                binding.linShowdata.setVisibility(View.VISIBLE);

                int completed=db.myCoursesDao().getAllMyCourses(savedid,true).size();
                int ongoing=db.myCoursesDao().getAllMyCourses(savedid,false).size();
                binding.tvEmail.setText(user.getEmail());
                binding.tvPhone.setText(String.valueOf(user.getPhoneNumber()));
                binding.tvJoinedin.setText("Joined on "+user.getJoinDate());
                binding.tvUserName.setText(user.getFirstName().toUpperCase());
                binding.tvCompletes.setText(completed+" Completed");
                binding.tvOngoing.setText(ongoing+" Ongoing");
                loadImageFromStorage(user.getUserImagePath(),binding.imageUserProfile);



            }
        });



        binding.btnBackFromedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.linEditdata.setVisibility(View.GONE);
                binding.linShowdata.setVisibility(View.VISIBLE);


            }
        });






        //-----------------------------------------------------------------------

        ActivityResultLauncher<Intent> lancher2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) { // تحقق من النتيجة
                            Intent intent = result.getData();
                            if (intent != null && intent.getData() != null) { // تحقق من أن البيانات ليست فارغة
                                imageUri = intent.getData();
                                binding.imageUserEdit.setImageURI(imageUri); // تعيين الصورة في ImageView
                            } else {
                                Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

// فتح المعرض عند الضغط
        binding.cardImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


    private Bitmap uriToBitmap(Uri uri, Context context) {
        try {
            // استخدم ContentResolver لتحميل الصورة كـ Bitmap
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // إذا حدث خطأ
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

    @Override
    public void onResume() {
        super.onResume();
        Appdatabase db=Appdatabase.getDatabase(getContext());
        User user=db.userDao().getUserByid(savedid);
        int completed=db.myCoursesDao().getAllMyCourses(savedid,true).size();
        int ongoing=db.myCoursesDao().getAllMyCourses(savedid,false).size();
        binding.tvEmail.setText(user.getEmail());
        binding.tvPhone.setText(String.valueOf(user.getPhoneNumber()));
        binding.tvJoinedin.setText("Joined on "+user.getJoinDate());
        binding.tvUserName.setText(user.getFirstName().toUpperCase());
        binding.tvCompletes.setText(completed+" Completed");
        binding.tvOngoing.setText(ongoing+" Ongoing");
        loadImageFromStorage(user.getUserImagePath(),binding.imageUserProfile);




    }
}