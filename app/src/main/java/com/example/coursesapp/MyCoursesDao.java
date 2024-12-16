package com.example.coursesapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyCoursesDao {

    @Insert
    void insertMyCourse(MyCourses myCourses);

    @Query("SELECT * FROM MyCourses where UserID=:userid")
    List<MyCourses> getAllMyCourses(String userid);
}
