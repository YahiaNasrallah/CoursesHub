package com.example.coursesapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Course.class,parentColumns = {"id"},childColumns = {"CourseID"}))
public class Lecture {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private int LectureNumber;
    @NonNull
    private String LectureLink;
    @NonNull
    private String Description;
    @NonNull
    private long CourseID;


    public Lecture(){

    }

    public Lecture(int lectureNumber, @NonNull String lectureLink, @NonNull String description, long courseID) {
        LectureNumber = lectureNumber;
        LectureLink = lectureLink;
        Description = description;
        CourseID = courseID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLectureNumber() {
        return LectureNumber;
    }

    public void setLectureNumber(int lectureNumber) {
        LectureNumber = lectureNumber;
    }

    @NonNull
    public String getLectureLink() {
        return LectureLink;
    }

    public void setLectureLink(@NonNull String lectureLink) {
        LectureLink = lectureLink;
    }

    @NonNull
    public String getDescription() {
        return Description;
    }

    public void setDescription(@NonNull String description) {
        Description = description;
    }

    public long getCourseID() {
        return CourseID;
    }

    public void setCourseID(long courseID) {
        CourseID = courseID;
    }
}
