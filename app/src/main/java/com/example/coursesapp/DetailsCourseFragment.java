package com.example.coursesapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursesapp.databinding.FragmentDetailsCourseBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;


public class DetailsCourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentDetailsCourseBinding binding;
    String gg;

    public DetailsCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DetailsCourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsCourseFragment newInstance(String param1) {
        DetailsCourseFragment fragment = new DetailsCourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Appdatabase db=Appdatabase.getDatabase(getContext());
        if (getArguments()!=null){
        mParam1 = getArguments().getString(ARG_PARAM1);
    }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsCourseBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot(); // هذا الجذر يتم إرجاعه

        Appdatabase db=Appdatabase.getDatabase(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            String data = bundle.getString("id");
            if (data != null) {


                Course course = db.courseDao().getCoursesByID(Long.parseLong(data));
                Toast.makeText(getContext(), course.getDetails(), Toast.LENGTH_SHORT).show();
                binding.tvDetails.setText(course.getDetails());
                Log.d("DetailsCourseFragment", "Updated Details Text: " + binding.tvDetails.getText().toString());


                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        binding.tvDetails.setText(course.getDetails());
                        binding.tvCategory.setText(course.getCategorynameshown());
                        binding.tvPrice.setText(course.getPrice());
                        binding.tHourse.setText(course.getHours());
                    });
                } else {
                    Toast.makeText(getContext(), "Fragment not attached to the activity!", Toast.LENGTH_SHORT).show();
                }

                // عرض البيانات
            }
        }




//        binding.tvDetails.setText(course.getDetails());
//        binding.tvCategory.setText(course.getCategorynameshown());
//        binding.tvPrice.setText(course.getPrice());
//        binding.tHourse.setText(course.getHours());


        return rootView;
    }
}