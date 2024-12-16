package com.example.coursesapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insertCategory(Category courses);

    @Query("SELECT * FROM Category WHERE CategoryName = :name")
    Category getCategoryByTitle(String name);

    @Query("SELECT * FROM Category WHERE id = :id")
    Category getCategoryById(long id);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);





    @Query("SELECT * FROM Category")
    List<Category> getAllCategory();
}
