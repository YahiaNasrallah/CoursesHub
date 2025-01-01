package com.example.coursesapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Category.class,parentColumns = {"id"},childColumns = {"CategoryID"}))

public class Course {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String Title;
    @NonNull
    private String Description;
    @NonNull
    private String InstructorName;
    @NonNull
    private String Price;
    private int NumberOfStudents;
    @NonNull
    private String Hours;
    @NonNull
    private String Details;
    @NonNull
    private int LectureNumber;

    private String categorynameshown;
    private String imagePath;
    @NonNull
    private long CategoryID;

    public Course(){

    }
    public Course(@NonNull String title, @NonNull String description, @NonNull String instructorName,@NonNull long categoryID) {
        this.Title = title;
        this.Description = description;
        this.InstructorName = instructorName;
        this.CategoryID=categoryID;
    }
    public Course(@NonNull String title, @NonNull String description, @NonNull String instructorName, @NonNull String price, @NonNull int numberOfStudents, @NonNull String hours, @NonNull String details,@NonNull int lectureNumber, @NonNull long categoryID,String categorynameshown) {
        this.Title = title;
        this.Description = description;
        this.InstructorName = instructorName;
        this.Price = price;
        this.NumberOfStudents = numberOfStudents;
        this.Hours = hours;
        this.Details = details;
        this.LectureNumber = lectureNumber;
        this.CategoryID = categoryID;
        this.categorynameshown=categorynameshown;
    }
    public Course(@NonNull String title, @NonNull String description, @NonNull String instructorName, @NonNull String price, @NonNull int numberOfStudents, @NonNull String hours, @NonNull String details,@NonNull int lectureNumber, @NonNull long categoryID,String categorynameshown,String imagePath) {
        this.Title = title;
        this.Description = description;
        this.InstructorName = instructorName;
        this.Price = price;
        this.NumberOfStudents = numberOfStudents;
        this.Hours = hours;
        this.Details = details;
        this.LectureNumber = lectureNumber;
        this.CategoryID = categoryID;
        this.categorynameshown=categorynameshown;
        this.imagePath=imagePath;
    }
    public String getCategorynameshown() {
        return categorynameshown;
    }

    public void setCategorynameshown(String categorynameshown) {
        this.categorynameshown = categorynameshown;
    }

    @NonNull
    public String getTitle() {
        return Title;
    }

    public void setTitle(@NonNull String title) {
        Title = title;
    }

    @NonNull
    public String getDescription() {
        return Description;
    }

    public void setDescription(@NonNull String description) {
        Description = description;
    }

    @NonNull
    public String getInstructorName() {
        return InstructorName;
    }

    public void setInstructorName(@NonNull String instructorName) {
        InstructorName = instructorName;
    }

    @NonNull
    public long getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(@NonNull long categoryID) {
        CategoryID = categoryID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getPrice() {
        return Price;
    }

    public void setPrice(@NonNull String price) {
        Price = price;
    }

    @NonNull
    public int getNumberOfStudents() {
        return NumberOfStudents;
    }

    public void setNumberOfStudents(@NonNull int numberOfStudents) {
        NumberOfStudents = numberOfStudents;
    }

    @NonNull
    public String getHours() {
        return Hours;
    }

    public void setHours(@NonNull String hours) {
        Hours = hours;
    }

    @NonNull
    public String getDetails() {
        return Details;
    }

    public void setDetails(@NonNull String details) {
        Details = details;
    }

    public int getLectureNumber() {
        return LectureNumber;
    }

    public void setLectureNumber(int lectureNumber) {
        LectureNumber = lectureNumber;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
