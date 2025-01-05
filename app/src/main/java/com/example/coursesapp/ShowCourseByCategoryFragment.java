package com.example.coursesapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursesapp.databinding.FragmentShowCourseByCategoryBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowCourseByCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowCourseByCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Appdatabase db;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    CourseAdapter adapter;
    long savedid;
    boolean flag=false;
    public ShowCourseByCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowCourseByCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowCourseByCategoryFragment newInstance(String param1, String param2) {
        ShowCourseByCategoryFragment fragment = new ShowCourseByCategoryFragment();
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
         savedid = preferences.getLong("savedid", 0);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentShowCourseByCategoryBinding binding = FragmentShowCourseByCategoryBinding.inflate(inflater, container, false);


        db=Appdatabase.getDatabase(getContext());
        binding.recycleShowcorsebycategory.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set adapter
        adapter = new CourseAdapter(getContext(), db.courseDao().getCoursesByCategory(db.categoryDao().getCategoryByTitle(mParam1).getId()), new CourseAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {
                for (int i = 0; i <db.myCoursesDao().getAllMyCourses(savedid).size() ; i++) {
                    if (db.myCoursesDao().getAllMyCourses(savedid).get(i).getId()==db.courseDao().getCoursesByCategory(db.categoryDao().getCategoryByTitle(mParam1).getId()).get(position).getId()){
                        flag=true;
                    }
                }
                if (flag){
                    Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
                    intent.putExtra("course_id",db.courseDao().getCoursesByCategory(db.categoryDao().getCategoryByTitle(mParam1).getId()).get(position).getId());
                    intent.putExtra("from","mycourses");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
                    intent.putExtra("course_id",db.courseDao().getCoursesByCategory(db.categoryDao().getCategoryByTitle(mParam1).getId()).get(position).getId());
                    intent.putExtra("from","home");
                    startActivity(intent);
                }
                flag=false;

            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.recycleShowcorsebycategory.setLayoutManager(layoutManager);
        binding.recycleShowcorsebycategory.setAdapter(adapter);



     return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}