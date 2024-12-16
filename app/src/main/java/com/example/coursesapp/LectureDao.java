package com.example.coursesapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LectureDao {

    @Insert
    void insertLecture(Lecture lecture);

    @Query("SELECT * FROM Lecture where CourseID=:courseid")
    List<Lecture> getAllLecturesByCourseID(String courseid);

}
