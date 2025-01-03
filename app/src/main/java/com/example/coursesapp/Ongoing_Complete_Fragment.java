package com.example.coursesapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursesapp.databinding.FragmentOngoingCompleteBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ongoing_Complete_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ongoing_Complete_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private boolean mParam1;
    private String mParam2;
    FragmentOngoingCompleteBinding binding;
    MycoursesAdapter adapter;
    Appdatabase db;
    long savedid;
    User user;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public Ongoing_Complete_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ongoing_Complete_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Ongoing_Complete_Fragment newInstance(boolean param1, String param2) {
        Ongoing_Complete_Fragment fragment = new Ongoing_Complete_Fragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        db=Appdatabase.getDatabase(getContext());


        preferences = requireContext().getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();
        savedid  = preferences.getLong("savedid", 0); // استرجاع البريد الإلكتروني
        user=db.userDao().getUserByid(savedid);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOngoingCompleteBinding.inflate(inflater, container, false);


        adapter = new MycoursesAdapter(getContext(), db.myCoursesDao().getAllMyCourses(savedid, mParam1), position -> {

            Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
            intent.putExtra("course_id", db.myCoursesDao().getAllMyCourses(savedid, mParam1).get(position).getCourseID());
            intent.putExtra("from", "mycourses");
            startActivity(intent);
        });

// تهيئة RecyclerView
        binding.recyclerCoursesCompletedOngoing.setAdapter(adapter);
        binding.recyclerCoursesCompletedOngoing.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerCoursesCompletedOngoing.setHasFixedSize(true);







        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        // إعادة تحميل الكورسات عند عودة الـ Fragment
        reloadCourses();
    }

    private void reloadCourses() {
        List<MyCourses> updatedCourses = db.myCoursesDao().getAllMyCourses(savedid, mParam1);
        adapter.updateCourses(updatedCourses); // تحديث قائمة الكورسات داخل الـ Adapter
    }
}