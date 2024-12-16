package com.example.coursesapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert
    void insertCourse(Course courses);

    @Query("SELECT * FROM Course WHERE Title = :title")
    Course getCourseByTitle(String title);

    @Query("SELECT * FROM Course WHERE  CategoryID = :categoryID")
    List<Course> getCoursesByCategory(long categoryID);

    @Query("SELECT * FROM Course")
    List<Course> getAllCourses();


}
