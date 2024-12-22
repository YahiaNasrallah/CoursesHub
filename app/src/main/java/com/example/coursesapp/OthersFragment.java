package com.example.coursesapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class OthersFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Appdatabase db;
    CourseAdapter adapter;

    public OthersFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OthersFragment newInstance(String param1, String param2) {
        OthersFragment fragment = new OthersFragment();
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

        View view=inflater.inflate(R.layout.fragment_others, container, false);
        db=Appdatabase.getDatabase(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclefrag4);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Set adapter
        adapter = new CourseAdapter(getContext(), db.courseDao().getCoursesByCategory(db.categoryDao().getCategoryByTitle("Others").getId()), new CourseAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {

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