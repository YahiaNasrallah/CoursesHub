package com.example.coursesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView searchResultsList;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Appdatabase db = Appdatabase.getDatabase(getContext());


        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.pager);

        // إنشاء الـ Adapter وإضافة الفراجمنتات
        vpadapter adapter = new vpadapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new EducationFragment(), "Education");
        adapter.addFragment(new EngineeringFragment(), "Engineering");
        adapter.addFragment(new BusinessFragment(), "Business");
        adapter.addFragment(new OthersFragment(), "Others");

        // إعداد ViewPager2 مع الـ Adapter
        viewPager.setAdapter(adapter);

        // تعيين التمرير الأفقي (الوضع الافتراضي)

        tabLayout.setupWithViewPager(viewPager);


        RecyclerView recyclerViewForyou = view.findViewById(R.id.recycle_foryou);
        searchResultsList = view.findViewById(R.id.searchResultsList);

        List<Course> courses = new ArrayList<>();


//        if (db.courseDao().getAllCourses().isEmpty()){
//
//        }else {
//            courses.add(db.courseDao().getAllCourses().get(db.courseDao().getAllCourses().size() - 1));
//        }
//
//       CourseAdapter adapter2 = new CourseAdapter(getContext(),courses, new CourseAdapter.ClickHandle() {
//            @Override
//            public void onItemClick(int position) {
//                Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
//                intent.putExtra("course_id",db.courseDao().getCoursesByCategory(db.categoryDao().getCategoryByTitle("Business").getId()).get(position).getId());
//                startActivity(intent);
//            }
//
//            @Override
//            public void onLongItemClick(int position) {
//
//            }
//        });
//        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
//        recyclerViewForyou.setLayoutManager(layoutManager);
//        recyclerViewForyou.setAdapter(adapter2);
//


        if (db.courseDao().getAllCourses().isEmpty()) {

        } else {
            courses.add(db.courseDao().getAllCourses().get(db.courseDao().getAllCourses().size() - 1));
        }
        androidx.appcompat.widget.SearchView searchView = view.findViewById(R.id.searchView);

        CourseAdapter adapter1 = new CourseAdapter(getContext(), courses, new CourseAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
        recyclerViewForyou.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewForyou.setAdapter(adapter1);



        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        List<coursecut> sampleData = new ArrayList<>();
        for (int i = 0; i < db.courseDao().getAllCourses().size(); i++) {
            sampleData.add(new coursecut(db.courseDao().getAllCourses().get(i).getId(),db.courseDao().getAllCourses().get(i).getTitle()));
        }

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.clearFocus();
                searchResultsList.setVisibility(View.GONE);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                searchResultsList.setVisibility(View.VISIBLE);


                List<coursecut> filteredList = new ArrayList<>();
                for (coursecut item : sampleData) {
                    if (item.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                searchadapter searchadapter=new searchadapter(getContext(), filteredList, new searchadapter.ClickHandle() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
                        intent.putExtra("course_id",filteredList.get(position).getId());
                        startActivity(intent);
                    }
                });

                searchResultsList.setLayoutManager(new LinearLayoutManager(getContext()));
                searchResultsList.setAdapter(searchadapter);




                return false;
            }
        });



        return view;
    }



}