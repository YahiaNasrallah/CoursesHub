package com.example.coursesapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = {"id"}, childColumns = {"UserID"})
        }
)
public class Notification {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private long UserID;
    @NonNull
    private String Message;

    public Notification(){

    }
    public Notification(long userID, @NonNull String message) {
        UserID = userID;
        Message = message;
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
}
