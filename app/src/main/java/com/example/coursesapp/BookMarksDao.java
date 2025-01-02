package com.example.coursesapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookMarksDao {

    @Insert
    void insertBookMark(BookMarks bookMarks);

    @Query("SELECT * FROM BookMarks where UserID=:userid")
    List<BookMarks> getAllBookMarks(long userid);

    @Query("SELECT * FROM BookMarks WHERE CourseID = :courseId AND UserID = :userId LIMIT 1")
    BookMarks getBookmarkByCourseAndUser(long courseId, long userId);


    @Query("DELETE FROM BookMarks WHERE CourseID = :courseID AND UserID = :userID")
    void deleteBookMark(long courseID, long userID);

    @Delete
    void delete(BookMarks bookmark);

}
