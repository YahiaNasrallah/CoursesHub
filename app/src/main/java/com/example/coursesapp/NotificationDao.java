package com.example.coursesapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert
    void insertNotification(Notification notification);

    @Query("SELECT * FROM Notification where UserID=:userid")
    List<Notification> getAllNotifications(long userid);

    @Update
    void updateNotification(Notification notification);
}
