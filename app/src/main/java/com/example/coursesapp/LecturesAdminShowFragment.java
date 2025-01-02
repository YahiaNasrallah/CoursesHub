    package com.example.coursesapp;

    import android.annotation.SuppressLint;
    import android.app.AlertDialog;
    import android.content.Intent;
    import android.graphics.Color;
    import android.graphics.drawable.ColorDrawable;
    import android.net.Uri;
    import android.os.Bundle;

    import androidx.annotation.NonNull;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.LinearLayoutManager;

    import android.view.ContextMenu;
    import android.view.LayoutInflater;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.EditText;
    import android.widget.Spinner;
    import android.widget.Toast;

    import com.example.coursesapp.databinding.FragmentLecturesAdminShowBinding;
    import com.google.android.material.button.MaterialButton;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Objects;

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link LecturesAdminShowFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class LecturesAdminShowFragment extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;
        private long courseid;
        LectureAdapterAdmin adapter;
        String selectedLecture;
        FragmentLecturesAdminShowBinding binding;
        Spinner lecturenum_spinner;
        EditText edLectureName, edLectureDescription, edLectureLink;
        MaterialButton btn_save;
        int selectedLectureint;
        List<String> integers;
        Appdatabase db;
        int pos;



        public LecturesAdminShowFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LecturesAdminShowFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static LecturesAdminShowFragment newInstance(String param1, String param2) {
            LecturesAdminShowFragment fragment = new LecturesAdminShowFragment();
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

             binding = FragmentLecturesAdminShowBinding.inflate(inflater, container, false);
             db=Appdatabase.getDatabase(getContext());
            GetAdapterCourse(db.lectureDao().getAllLecturesByCourseID(courseid));



            binding.btnAddLectAdmin.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MissingInflatedId")
                @Override
                public void onClick(View view) {
    //            Intent intent=new Intent(getContext(), AddLectureActivity.class);
    //            intent.putExtra("id",courseid);
    //            startActivity(intent);



                    View coustem= LayoutInflater.from(getContext()).inflate(R.layout.log2,null);
                    AlertDialog.Builder bulder=new AlertDialog.Builder(getContext());
                    bulder.setView(coustem);

                     lecturenum_spinner=coustem.findViewById(R.id.lecturenum_spinner);
                     edLectureName =coustem.findViewById(R.id.ed_Lecture_name);
                     edLectureLink =coustem.findViewById(R.id.ed_Lecture_link);
                     edLectureDescription =coustem.findViewById(R.id.ed_Lecture_description);
                     btn_save=coustem.findViewById(R.id.btn_save_itrem);




                    AlertDialog dialog=bulder.create();
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);
                    dialog.show();

                    Course selectedCourse=db.courseDao().getCoursesByID(courseid);
                    List<String> integers=new ArrayList<>();
                    for (int i = 0; i <selectedCourse.getLectureNumber() ; i++) {
                        integers.add(String.valueOf(i+1));
                    }

                    ArrayAdapter<String> adapter3 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, integers);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lecturenum_spinner.setAdapter(adapter3);


                    lecturenum_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            // التعامل مع العنصر الذي تم اختياره

                            selectedLecture = integers.get(position); // احصل على العنصر المحدد

                            try {
                                selectedLectureint = Integer.parseInt(selectedLecture); // تحويل النص إلى رقم
                            } catch (NumberFormatException e) {
                                selectedLectureint = 1; // قيمة افتراضية
                                Toast.makeText(getContext(), "Choose Lecture Number", Toast.LENGTH_SHORT).show();
                            }



                            //Toast.makeText(AddLectureActivity.this, selectedItem, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            lecturenum_spinner   .setSelection(0);
                        }
                    });




                           btn_save.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   if (edLectureName.getText().toString().isEmpty()) {
                                       edLectureName.setError("Enter Lecture Name");
                                       edLectureName.requestFocus();
                                   }else if (edLectureLink.getText().toString().isEmpty()) {
                                       edLectureLink.setError("Enter Lecture Link");
                                       edLectureLink.requestFocus();

                                   }else if (edLectureDescription.getText().toString().isEmpty()) {
                                       edLectureDescription.setError("Enter Lecture Description");
                                       edLectureDescription.requestFocus();
                                   }else {
                                        if (selectedLecture==null) {
                                           Toast.makeText(getContext(), "Choose Lecture Number", Toast.LENGTH_SHORT).show();
                                       } else if (edLectureName.getText().toString().isEmpty()) {
                                           edLectureName.setError("Enter Lecture Name");
                                           edLectureName.requestFocus();
                                       }else if (edLectureLink.getText().toString().isEmpty()) {
                                           edLectureLink.setError("Enter Lecture Link");
                                           edLectureLink.requestFocus();
                                       }else if (edLectureDescription.getText().toString().isEmpty()) {
                                           edLectureDescription.setError("Enter Lecture Description");
                                           edLectureDescription.requestFocus();
                                       } else {


                                           if (edLectureLink.getText().toString().startsWith("https://www.youtube.com/") || edLectureLink.getText().toString().startsWith("https://youtu.be/")){




                                               List<Lecture> lectures = db.lectureDao().getAllLecturesByCourseID(
                                                       courseid
                                               );

                                               boolean lectureExists = false;

                                               for (Lecture lecture : lectures) {
                                                   if (lecture.getLectureNumber() == selectedLectureint) {
                                                       lectureExists = true;
                                                       break;
                                                   }
                                               }

                                               if (lectureExists) {
                                                   Toast.makeText(getContext(), "Lecture Already Exists", Toast.LENGTH_SHORT).show();
                                               } else {
                                                   Lecture lecture = new Lecture();
                                                   lecture.setCourseID(courseid);
                                                   lecture.setLectureNumber(selectedLectureint);
                                                   lecture.setLectureName(edLectureName.getText().toString());
                                                   lecture.setLectureLink(edLectureLink.getText().toString());
                                                   lecture.setDescription(edLectureDescription.getText().toString());

                                                   db.lectureDao().insertLecture(lecture);
                                                   Toast.makeText(getContext(), "Lecture Added", Toast.LENGTH_SHORT).show();
                                                   dialog.dismiss();
                                                   GetAdapterCourse(db.lectureDao().getAllLecturesByCourseID(courseid));

                                               }

                                           }else {
                                               edLectureLink.setError("The Lecture link is invalid");
                                               edLectureLink.requestFocus();
                                           }


                                       }

                                   }


                               }
                           });






                       }
                   });








            return binding.getRoot();
        }
        public void updateData(long courseid) {
            this.courseid = courseid;




        }


        @Override
        public void onResume() {
            super.onResume();
            GetAdapterCourse(db.lectureDao().getAllLecturesByCourseID(courseid));
        }



        @Override
        public boolean onContextItemSelected(@NonNull MenuItem item) {
            db = Appdatabase.getDatabase(getContext());
            int id = item.getItemId();

            if (id == R.id.menu_delete) {
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("تأكيد العملية")
                        .setMessage("هل أنت متأكد أنك تريد المتابعة؟")
                        .setPositiveButton("نعم", (dialog, which) -> {

                            db.lectureDao().deleteLecture(db.lectureDao().getAllLecturesByCourseID(courseid).get(pos));
                            adapter.notifyItemRemoved(pos);
                            GetAdapterCourse(db.lectureDao().getAllLecturesByCourseID(courseid));



                        })
                        .setNegativeButton("لا", (dialog, which) -> dialog.dismiss())
                        .show();
            } else if (id == R.id.menu_edit) {


            }
            return super.onContextItemSelected(item);
        }

        @Override
        public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflatur=requireActivity().getMenuInflater();
            inflatur.inflate(R.menu.menu1,menu);

        }



        void GetAdapterCourse(List<Lecture> lectureList) {

            adapter =new LectureAdapterAdmin(getContext(), lectureList, new LectureAdapterAdmin.ClickHandle() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(db.lectureDao().getAllLecturesByCourseID(courseid).get(position).getLectureLink()));
                    startActivity(intent);
                }

                @Override
                public void onLongItemClick(int position) {

                    pos = position; // تعيين الموضعJeff
                    registerForContextMenu(Objects.requireNonNull(binding.recycleLecturesAdmin.findViewHolderForAdapterPosition(position)).itemView);
                }
            });

            binding.recycleLecturesAdmin.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
            binding.recycleLecturesAdmin.setLayoutManager(linearLayoutManager);

        }


    }


