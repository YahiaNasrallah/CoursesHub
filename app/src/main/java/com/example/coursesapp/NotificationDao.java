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
    List<Notification> getAllNotificationsByUser(long userid);


    @Query("SELECT * FROM Notification where courseID=:courseid")
    List<Notification> getAllNotificationsByCOurse(long courseid);

    @Query("DELETE FROM Notification WHERE courseID = :courseId")
    void deleteNotificationByCourseID(long courseId);


    @Update
    void updateNotification(Notification notification);
}
