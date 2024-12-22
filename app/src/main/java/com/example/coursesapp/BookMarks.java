package com.example.coursesapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = {"id"}, childColumns = {"CourseID"}),
                @ForeignKey(entity = User.class, parentColumns = {"id"}, childColumns = {"UserID"})
        }
)
public class BookMarks {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    @NonNull
    private long CourseID;
    @NonNull
    private long UserID;

    public BookMarks(){

    }
    public BookMarks(long courseID, long userID) {
        CourseID = courseID;
        UserID = userID;
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
}
