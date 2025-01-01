package com.example.coursesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coursesapp.databinding.FragmentCourseDetailsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    long courseid;
    long userid;
    Appdatabase db;
    boolean flag;

    public CourseDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */


    // TODO: Rename and change types and number of parameters
    public static CourseDetailsFragment newInstance(String param1, String param2) {
        CourseDetailsFragment fragment = new CourseDetailsFragment();
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            long data = bundle.getLong("id");
            // تخزين البيانات في متغير داخلي
            this.courseid = data;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCourseDetailsBinding binding = FragmentCourseDetailsBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment

        Appdatabase db=Appdatabase.getDatabase(getContext());

        if (courseid != 0) {
            Course course = db.courseDao().getCoursesByID(courseid);
            User user = db.userDao().getUserByid(userid);
            binding.tvCourseCategory.setText(course.getCategorynameshown());
            binding.tvCourseHouse.setText(String.valueOf(course.getHours()));
            binding.tvCoursePrice.setText(String.valueOf(course.getPrice()));
            binding.tvCourseDetails.setText(course.getDetails());
            binding.tvCourseAuthor.setText(course.getInstructorName());
            binding.tvCourseStdnum.setText(String.valueOf(course.getNumberOfStudents()));



            if (!flag){
                binding.btnSignToCourse.setVisibility(View.GONE);
            }else {
                binding.btnSignToCourse.setVisibility(View.VISIBLE);
                binding.btnSignToCourse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        boolean bb = true;

                        for (int i = 0; i < db.myCoursesDao().getAllMyCourses(userid).size(); i++) {
                            if (db.myCoursesDao().getAllMyCourses(userid).get(i).getCourseID() == course.getId() && db.myCoursesDao().getAllMyCourses(userid).get(i).getUserID() == user.getId()) {
                                Toast.makeText(getContext(), "You are already sign this course", Toast.LENGTH_SHORT).show();
                                bb = false;
                            }

                        }
                        if (bb) {
                            MyCourses myCourses = new MyCourses();
                            myCourses.setCourseID(course.getId());
                            myCourses.setUserID(user.getId());
                            myCourses.setProgress(0);
                            myCourses.setCompleted(false);
                            Course course1=db.courseDao().getCoursesByID(courseid);
                            course1.setNumberOfStudents(course1.getNumberOfStudents()+1);
                            db.courseDao().updateCourse(course1);
                            db.myCoursesDao().insertMyCourse(myCourses);
                            Toast.makeText(getContext(), "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();
                            requireActivity().finish(); // إنهاء الـ Activity من داخل الـ Fragment

                        }

                    }
                });

            }



        }



        return binding.getRoot();
    }

    public void updateData(long courseid,long userid,boolean flag) {
        this.courseid = courseid;
        this.userid = userid;
        this.flag=flag;



    }
}