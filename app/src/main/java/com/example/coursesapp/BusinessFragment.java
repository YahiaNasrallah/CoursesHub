package com.example.coursesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BusinessFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Appdatabase db;
    CourseAdapter adapter;

    public BusinessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusinessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BusinessFragment newInstance(String param1, String param2) {
        BusinessFragment fragment = new BusinessFragment();
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
        View view=inflater.inflate(R.layout.fragment_business, container, false);

        db=Appdatabase.getDatabase(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclefrag3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Set adapter
        adapter = new CourseAdapter(getContext(), db.courseDao().getCoursesByCategory(db.categoryDao().getCategoryByTitle("Business").getId()), new CourseAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
                intent.putExtra("course_id",db.courseDao().getCoursesByCategory(db.categoryDao().getCategoryByTitle("Business").getId()).get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);




        return view;
    }
}