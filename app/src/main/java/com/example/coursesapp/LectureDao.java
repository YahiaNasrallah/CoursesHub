package com.example.coursesapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LectureDao {

    @Insert
    void insertLecture(Lecture lecture);

    @Query("SELECT * FROM Lecture where CourseID=:courseid")
    List<Lecture> getAllLecturesByCourseID(long courseid);

    @Delete
    void deleteLecture(Lecture lecture);

    @Query("SELECT * FROM Lecture where id=:id")
    Lecture getLectureByID(long id);

    @Update
    void updateLecture(Lecture lecture);

    @Query("DELETE FROM Lecture WHERE courseID = :courseId")
    void deleteLecturesByCourseID(long courseId);
}
