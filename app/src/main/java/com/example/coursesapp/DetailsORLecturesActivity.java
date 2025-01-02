package com.example.coursesapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.coursesapp.databinding.ActivityDetailsOrlecturesBinding;
import com.google.android.material.tabs.TabLayout;

public class DetailsORLecturesActivity extends AppCompatActivity {

    ActivityDetailsOrlecturesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetailsOrlecturesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        long id=getIntent().getLongExtra("id",0);





        binding.tabLayout.setupWithViewPager(binding.pagerAdminLec);
        vpadapter adapter=new vpadapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new ShowEditCourseAdminFragment(),"Details");
        adapter.addFragment(new LecturesAdminShowFragment(),"Lectures");
        binding.pagerAdminLec.setAdapter(adapter);


        ShowEditCourseAdminFragment fragment = (ShowEditCourseAdminFragment) adapter.getFragmentAt(0);
        if (fragment != null) {
            fragment.updateData(id);
        }


        LecturesAdminShowFragment fragment2 = (LecturesAdminShowFragment) adapter.getFragmentAt(1);
        if (fragment2 != null) {
            fragment2.updateData(id);
        }


    }
}