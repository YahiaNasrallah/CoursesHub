package com.example.coursesapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = {"id"}, childColumns = {"UserID"}),
                @ForeignKey(entity = Course.class, parentColumns = {"id"}, childColumns = {"courseID"})

        }
)
public class Notification {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    @NonNull
    private long UserID;
    @NonNull
    private long courseID;
    @NonNull
    private String Title;
    @NonNull
    private String Message;

    @NonNull
    private boolean isNotified;

    public Notification(){

    }
    public Notification(long userID,@NonNull long coourseID,@NonNull String title ,@NonNull String message ,@NonNull boolean isNotified) {
        this.UserID = userID;
        this.Message = message;
        this.Title=title;
        this.isNotified=isNotified;
        this.courseID =coourseID;

    }

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long userID) {
        UserID = userID;
    }

    @NonNull
    public String getMessage() {
        return Message;
    }

    public void setMessage(@NonNull String message) {
        Message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    @NonNull
    public String getTitle() {
        return Title;
    }

    public void setTitle(@NonNull String title) {
        Title = title;
    }
}
