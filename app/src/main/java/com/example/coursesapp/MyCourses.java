package com.example.coursesapp;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = Category.class, parentColumns = {"id"}, childColumns = {"CourseID"}),
                @ForeignKey(entity = User.class, parentColumns = {"id"}, childColumns = {"UserID"})
        }
)
public class MyCourses {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private int Progress;

    @NonNull
    private boolean Completed;

    @NonNull
    private long CourseID;

    @NonNull
    private long UserID;

    private ArrayList<Long> completedLectures; // قائمة المحاضرات المكتملة

    public MyCourses(){
        completedLectures = new ArrayList<>();

    }

    public MyCourses(int progress, boolean completed, long courseID, long userID) {
        Progress = progress;
        Completed = completed;
        CourseID = courseID;
        UserID = userID;
        completedLectures = new ArrayList<>();

    }

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }

    public boolean isCompleted() {
        return Completed;
    }

    public void setCompleted(boolean completed) {
        Completed = completed;
    }

    public long getCourseID() {
        return CourseID;
    }

    public void setCourseID(long courseID) {
        CourseID = courseID;
    }

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long userID) {
        UserID = userID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Long> getCompletedLectures() {
        return completedLectures;
    }

    public void setCompletedLectures(ArrayList<Long> completedLectures) {
        this.completedLectures = completedLectures;
    }
}
