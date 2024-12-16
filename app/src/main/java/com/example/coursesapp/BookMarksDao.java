package com.example.coursesapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookMarksDao {

    @Insert
    void insertBookMark(BookMarks bookMarks);

    @Query("SELECT * FROM BookMarks where UserID=:userid")
    List<BookMarks> getAllBookMarks(String userid);

}
