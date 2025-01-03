package com.example.coursesapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCoursesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Appdatabase db;
    MycoursesAdapter adapter;
    long savedid;
    User user;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyCoursesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCoursesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCoursesFragment newInstance(String param1, String param2) {
        MyCoursesFragment fragment = new MyCoursesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Appdatabase db=Appdatabase.getDatabase(getContext());

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
        View view=inflater.inflate(R.layout.fragment_my_courses, container, false);

        db=Appdatabase.getDatabase(getContext());


        TabLayout tabLayout=view.findViewById(R.id.tab_layout_courses);
        ViewPager viewPager=view.findViewById(R.id.pager_courses);

        tabLayout.setupWithViewPager(viewPager);
        vpadapter adapter=new vpadapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(Ongoing_Complete_Fragment.newInstance(false,"null"),"Ongoing");
        adapter.addFragment(Ongoing_Complete_Fragment.newInstance(true,"null"),"Completed");
        viewPager.setAdapter(adapter);





        return view;
    }
}