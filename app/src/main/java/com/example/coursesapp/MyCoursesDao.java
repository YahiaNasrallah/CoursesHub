package com.example.coursesapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyCoursesDao {

    @Insert
    void insertMyCourse(MyCourses myCourses);

    @Query("SELECT * FROM MyCourses where UserID=:userid")
    List<MyCourses> getAllMyCourses(long userid);

    @Query("SELECT * FROM MyCourses where UserID=:userid and Completed=:cmompleted")
    List<MyCourses> getAllMyCourses(long userid, boolean cmompleted);


    @Query("SELECT * FROM MyCourses where UserID=:userid and CourseID=:courseid")
    MyCourses getMyCourseByCourseIDAndUserID(long userid, long courseid);

    @Query("SELECT * FROM MyCourses where CourseID=:courseid")
    MyCourses getMyCourseByCourseID(long courseid);

    @Query("SELECT * FROM MyCourses where CourseID=:courseid")
    List<MyCourses> getAllMyCourseByCourseID(long courseid);

    @Query("SELECT * FROM MyCourses where UserID=:userid")
    List<MyCourses> getMyCourseByUserID(long userid);


    @Query("DELETE FROM MyCourses where UserID=:userid and CourseID=:courseid")
    void deleteMyCourse(long userid, long courseid);

    @Query("DELETE FROM MyCourses where CourseID=:courseid")
    void deleteMyCourseByCourseID(long courseid);


    @Update
    void updateMyCourse(MyCourses myCourses);


}
