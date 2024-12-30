package com.example.coursesapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursesapp.databinding.FragmentCoursesOngoingBinding;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesOngoingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesOngoingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MycoursesAdapter adapter;
    Appdatabase db;
    long savedid;
    User user;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private RecyclerView recyclerView;



    public CoursesOngoingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LecturesOngoingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesOngoingFragment newInstance(String param1, String param2) {
        CoursesOngoingFragment fragment = new CoursesOngoingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=Appdatabase.getDatabase(getContext());

        preferences = requireContext().getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();
        savedid  = preferences.getLong("savedid", 0); // استرجاع البريد الإلكتروني
       user=db.userDao().getUserByid(savedid);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_courses_ongoing, container, false);
        recyclerView = view.findViewById(R.id.recycler_courses_ongoing); // تهيئة RecyclerView هنا
        adapter = new MycoursesAdapter(getContext(), db.myCoursesDao().getAllMyCourses(savedid, false), position -> {
            Intent intent = new Intent(getContext(), CourseUserINActivity.class);
            intent.putExtra("course_id", db.myCoursesDao().getAllMyCourses(savedid, false).get(position).getCourseID());
            startActivity(intent);
        });

// تهيئة RecyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);





        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        // إعادة تحميل الكورسات عند عودة الـ Fragment
        reloadCourses();
    }

    private void reloadCourses() {
        List<MyCourses> updatedCourses = db.myCoursesDao().getAllMyCourses(savedid, false);
        adapter.updateCourses(updatedCourses); // تحديث قائمة الكورسات داخل الـ Adapter
    }


}