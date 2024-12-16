package com.example.coursesapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(

)
public class Category {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String CategoryName;

    private String CategoryImage;

    public Category(){

    }

    public Category(@NonNull String categoryName){
        this.CategoryName=categoryName;
    }

    public Category(@NonNull String categoryName, String image){
        this.CategoryName=categoryName;
        this.CategoryImage=image;
    }

    @NonNull
    public String getCategoryName() {
        return CategoryName;
    }



    public void setCategoryName(@NonNull String categoryName) {
        CategoryName = categoryName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }
}
