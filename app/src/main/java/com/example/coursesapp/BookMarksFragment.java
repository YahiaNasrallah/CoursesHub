package com.example.coursesapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursesapp.databinding.FragmentBookMarksBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookMarksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookMarksFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentBookMarksBinding binding;
    BookmarkAdapter adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Appdatabase db;
    public BookMarksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookMarksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookMarksFragment newInstance(String param1, String param2) {
        BookMarksFragment fragment = new BookMarksFragment();
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
         binding=FragmentBookMarksBinding.inflate(inflater,container,false);
         db=Appdatabase.getDatabase(getContext());

        preferences = requireContext().getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();
        long savedid = preferences.getLong("savedid", 0);

        adapter=new BookmarkAdapter(getContext(), db.bookMarksDao().getAllBookMarks(savedid), new BookmarkAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
        binding.recycleBookmark.setAdapter(adapter);
        binding.recycleBookmark.setHasFixedSize(true);
        GridLayoutManager manager=new GridLayoutManager(getContext(),2);
        binding.recycleBookmark.setLayoutManager(manager);



                return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        db=Appdatabase.getDatabase(getContext());
        preferences = requireContext().getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();
        long savedid = preferences.getLong("savedid", 0);

        adapter=new BookmarkAdapter(getContext(), db.bookMarksDao().getAllBookMarks(savedid), new BookmarkAdapter.ClickHandle() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
        binding.recycleBookmark.setAdapter(adapter);
        binding.recycleBookmark.setHasFixedSize(true);
        GridLayoutManager manager=new GridLayoutManager(getContext(),2);
        binding.recycleBookmark.setLayoutManager(manager);



    }
}