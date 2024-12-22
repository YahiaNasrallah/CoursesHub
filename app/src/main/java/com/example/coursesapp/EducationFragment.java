package com.example.coursesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coursesapp.databinding.FragmentEducationBinding;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EducationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EducationFragment extends Fragment {

    //
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    Appdatabase db;
    CourseAdapter adapter;

    public EducationFragment() {
    }


    public static EducationFragment newInstance(String param1, String param2) {
        EducationFragment fragment = new EducationFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_education, container, false);
         db=Appdatabase.getDatabase(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclefrag1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Set adapter
        adapter = new CourseAdapter(getContext(), db.courseDao().getCoursesByCategory(db.categoryDao().getCategoryByTitle("Education").getId()), new CourseAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
                intent.putExtra("course_id",db.courseDao().getCoursesByCategory(db.categoryDao().getCategoryByTitle("Education").getId()).get(position).getId());
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);





        return view;
    }


}