package com.example.coursesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coursesapp.databinding.FragmentLecturesBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LecturesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LecturesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    long courseId;
    long userId;
    boolean status;
    LectureAdapter adapter;
    Appdatabase db;

    public LecturesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LecturesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LecturesFragment newInstance(String param1, String param2) {
        LecturesFragment fragment = new LecturesFragment();
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            long data = bundle.getLong("id");
            // تخزين البيانات في متغير داخلي
            this.courseId = data;
            this.userId = bundle.getLong("userid");
            this.status = bundle.getBoolean("status");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentLecturesBinding binding=FragmentLecturesBinding.inflate(inflater,container,false);
        db=Appdatabase.getDatabase(getContext());


        if (status){
            binding.linLock.setVisibility(View.GONE);
            binding.recycleLectures.setVisibility(View.VISIBLE);
            adapter=new LectureAdapter(getContext(), db.lectureDao().getAllLecturesByCourseID(courseId), new LectureAdapter.ClickHandle() {
                @Override
                public void onItemClick(int position) {
                    Lecture lecture=db.lectureDao().getAllLecturesByCourseID(courseId).get(position);
                    if (lecture.getLectureLink().startsWith("https://www.youtube.com/")||lecture.getLectureLink().startsWith("https://youtu.be/")){


                       Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(lecture.getLectureLink()));
                        startActivity(intent);
                        Course course=db.courseDao().getCoursesByID(courseId);
                     //   MyCourses myCourses=db.myCoursesDao().getMyCourseByCourseIDAndUserID(userid,courseid);
                        MyCourses myCourse = db.myCoursesDao().getMyCourseByCourseIDAndUserID(userId, courseId);

                        new Thread(() -> updateCourseProgress(lecture)).start();






                        new Thread(() -> {
                            MyCourses myCourses = db.myCoursesDao().getMyCourseByCourseIDAndUserID(userId, courseId);

                            if (myCourses != null) {
                                if (myCourses.getCompletedLectures() == null) {
                                    myCourses.setCompletedLectures(new ArrayList<>());
                                }

                                // تحقق إذا كانت المحاضرة غير مكتملة
                                if (!myCourses.getCompletedLectures().contains(lecture.getId())) {
                                    myCourses.getCompletedLectures().add(lecture.getId()); // أضف المحاضرة
                                    int progress = (myCourses.getCompletedLectures().size() * 100) / course.getLectureNumber();
                                    myCourses.setProgress(progress); // تحديث النسبة المئوية

                                    // تحقق إذا تم إكمال الكورس
                                    if (myCourses.getCompletedLectures().size() == course.getLectureNumber()) {
                                        myCourses.setCompleted(true);
                                        myCourses.setCompleteDate(getFormattedDate());

// الكورس مكتمل
                                    }

                                    // تحديث في قاعدة البيانات
                                    db.myCoursesDao().updateMyCourse(myCourses);
                                }
                            }
                        }).start();






//                        new Thread(() -> {
//                            MyCourses myCourse = db.myCoursesDao().getMyCourseByCourseIDAndUserID(userid, courseid);
//                            if (myCourse != null) {
//                                int totalLectures = course.getLectureNumber(); // العدد الإجمالي للمحاضرات
//                                int progress = (myCourse.getCompletedLectures().size() * 100) / totalLectures;
//                                Toast.makeText(getContext(), "Progress: " + progress + "%", Toast.LENGTH_SHORT).show();
//                            }
//                        }).start();




//                        Toast.makeText(getContext(),String.valueOf(myCourses.getProgress()), Toast.LENGTH_SHORT).show();
//                        myCourses.setProgress(progress);
//                        db.myCoursesDao().updateMyCourse(myCourses);
//                        Toast.makeText(getContext(),String.valueOf(myCourses.getProgress()), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "Lecture Link is not valid", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onLongItemClick(int position) {

                }
            });

            binding.recycleLectures.setAdapter(adapter);
            LinearLayoutManager manager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            binding.recycleLectures.setLayoutManager(manager);


        }else {
            binding.linLock.setVisibility(View.VISIBLE);
            binding.recycleLectures.setVisibility(View.GONE);
        }








        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadLectures();
    }

    private void reloadLectures() {
        if (status) {
            // استرجاع المحاضرات المحدثة من قاعدة البيانات
            List<Lecture> updatedLectures = db.lectureDao().getAllLecturesByCourseID(courseId);
            adapter.updateLectures(updatedLectures); // تحديث الـ Adapter بالمحاضرات المحدثة
        }
    }

    public void uUpdateData(long courseid,long userid,boolean status) {
        if (status) {
            this.courseId = courseid;
            this.userId = userid;
            this.status = status;
        }



    }


    private void updateCourseProgress(Lecture lecture) {
        if (status) {
            MyCourses myCourse = db.myCoursesDao().getMyCourseByCourseIDAndUserID(userId, courseId);
            if (myCourse != null) {
                if (myCourse.getCompletedLectures() == null) {
                    myCourse.setCompletedLectures(new ArrayList<>());
                }

                if (!myCourse.getCompletedLectures().contains(lecture.getId())) {
                    myCourse.getCompletedLectures().add(lecture.getId());
                    Course course = db.courseDao().getCoursesByID(courseId);

                    int progress = (myCourse.getCompletedLectures().size() * 100) / course.getLectureNumber();
                    myCourse.setProgress(progress);

                    if (myCourse.getCompletedLectures().size() == course.getLectureNumber()) {
                        myCourse.setCompleted(true);
                    }

                    db.myCoursesDao().updateMyCourse(myCourse);
                }
            }
        }
    }
    public static String getFormattedDate() {
        // إنشاء كائن تاريخ يحتوي على الوقت الحالي
        Date currentDate = new Date();

        // التنسيق المطلوب: اليوم رقم، الشهر كلمة، السنة
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", new Locale("en"));

        // تحويل التاريخ إلى النص المطلوب
        return dateFormat.format(currentDate);
    }



}