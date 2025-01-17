package com.example.coursesapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView searchResultsList;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ShowCourseByCategoryFragment fragment;
    long savedid;
    boolean flag=false;
    boolean flag2=false;
    NotificationAdapter adapter;


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
        preferences = requireContext().getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();
        savedid  = preferences.getLong("savedid", 0);

        Appdatabase db = Appdatabase.getDatabase(getContext());
        User user=db.userDao().getUserByid(savedid);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        preferences = requireContext().getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();
        savedid  = preferences.getLong("savedid", 0);

        CardView btn_notification=view.findViewById(R.id.btn_notification);
        Appdatabase db = Appdatabase.getDatabase(getContext());


        View coustem= LayoutInflater.from(getContext()).inflate(R.layout.notification_dialog_item,null);
        AlertDialog.Builder bulder=new AlertDialog.Builder(getContext());
        bulder.setView(coustem);
        AlertDialog dialog=bulder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        RecyclerView recyclerView=coustem.findViewById(R.id.recycle_notification);

        List<Notification> notifications = db.notificationDao().getAllNotificationsByUser(savedid);
        Collections.reverse(notifications);

        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
                adapter=new NotificationAdapter(getContext(),notifications, new NotificationAdapter.ClickHandle() {
                    @Override
                    public void onItemClick(int position) {

                    }

                    @Override
                    public void onLongItemClick(int position) {

                    }
                });
                LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);

            }
        });




        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.pager);

        // إنشاء الـ Adapter وإضافة الفراجمنتات
        vpadapter adapter = new vpadapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment( ShowCourseByCategoryFragment.newInstance("Education","null"), "Education");
        adapter.addFragment( ShowCourseByCategoryFragment.newInstance("Engineering","null"), "Engineering");
        adapter.addFragment( ShowCourseByCategoryFragment.newInstance("Business","null"), "Business");
        adapter.addFragment( ShowCourseByCategoryFragment.newInstance("Other","null"), "Other");

        // إعداد ViewPager2 مع الـ Adapter
        viewPager.setAdapter(adapter);

        // تعيين التمرير الأفقي (الوضع الافتراضي)

        tabLayout.setupWithViewPager(viewPager);


        RecyclerView recyclerViewForyou = view.findViewById(R.id.recycle_foryou);
        searchResultsList = view.findViewById(R.id.searchResultsList);




        //----------------Foryou-----------------------
        List<Course> courses = new ArrayList<>();

        if (db.courseDao().getAllCourses().isEmpty()) {

        } else {
            if (db.courseDao().getAllCourses().size()>=2){
                courses.add(db.courseDao().getAllCourses().get(db.courseDao().getAllCourses().size() - 2));
                courses.add(db.courseDao().getAllCourses().get(db.courseDao().getAllCourses().size() - 1));
            }else {
                courses.add(db.courseDao().getAllCourses().get(db.courseDao().getAllCourses().size() - 1));

            }
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









        //------------------searchbar-------------------

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

                        for (int i = 0; i <db.myCoursesDao().getAllMyCourses(savedid).size() ; i++) {
                            if (db.myCoursesDao().getAllMyCourses(savedid).get(i).getCourseID()==filteredList.get(position).getId()){
                                flag=true;
                            }
                        }
                        if (flag){
                            Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
                            intent.putExtra("course_id",filteredList.get(position).getId());
                            intent.putExtra("from","mycourses");
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
                            intent.putExtra("course_id",filteredList.get(position).getId());
                            intent.putExtra("from","home");
                            startActivity(intent);
                        }
                        flag=false;



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