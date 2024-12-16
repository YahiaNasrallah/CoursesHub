package com.example.coursesapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insertCategory(Category courses);

    @Query("SELECT * FROM Category WHERE CategoryName = :name")
    Category getCategoryByTitle(String name);





    @Query("SELECT * FROM Category")
    List<Category> getAllCategory();
}
