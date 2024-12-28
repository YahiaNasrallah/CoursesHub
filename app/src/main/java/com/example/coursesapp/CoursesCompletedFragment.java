package com.example.coursesapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coursesapp.databinding.FragmentCoursesCompletedBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesCompletedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesCompletedFragment extends Fragment {

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

    public CoursesCompletedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LectureesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesCompletedFragment newInstance(String param1, String param2) {
        CoursesCompletedFragment fragment = new CoursesCompletedFragment();
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
        View view=inflater.inflate(R.layout.fragment_courses_completed, container, false);
        RecyclerView recyclerView =view.findViewById(R.id.recycler_courses_completed);
//        MyCourses myCourses=new MyCourses();
//        myCourses.setCourseID(db.myCoursesDao().getAllMyCourses(savedid).get(1).getCourseID());
//        myCourses.setUserID(db.myCoursesDao().getAllMyCourses(savedid).get(1).getUserID());
//        myCourses.setId(db.myCoursesDao().getAllMyCourses(savedid).get(1).getId());
//        myCourses.setCompleted(true);
//        myCourses.setProgress(100);
//        db.myCoursesDao().updateMyCourse(myCourses);




        adapter=new MycoursesAdapter(getContext(), db.myCoursesDao().getAllMyCourses(savedid,true), new MycoursesAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), CourseUserINActivity.class);
                intent.putExtra("course_id",db.myCoursesDao().getAllMyCourses(savedid,true).get(position).getCourseID());
                startActivity(intent);


            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);




        return view;
    }

    @Override
    public void onResume() {

        if (recyclerView != null) {
            // إعداد الـ Adapter
            adapter = new MycoursesAdapter(getContext(), db.myCoursesDao().getAllMyCourses(savedid, true), new MycoursesAdapter.ClickHandle() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getContext(), CourseUserINActivity.class);
                    intent.putExtra("course_id", db.myCoursesDao().getAllMyCourses(savedid, true).get(position).getCourseID());
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Log.e("CoursesCompletedFragment", "RecyclerView is null in onResume");
        }

        super.onResume();
    }
}