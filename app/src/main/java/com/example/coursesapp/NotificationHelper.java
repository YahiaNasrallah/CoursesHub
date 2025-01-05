package com.example.coursesapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {


    public static void addCourse(Context context, String courseName, String userName) {
        String message = "Hello " + userName + ", We Added a New Course: " + courseName + ". We Think You’re Interested In.";
        NotificationHelper.showNotification(context, "New Course Added", message);
    }

    public static void addLecture(Context context, String lectureTitle, String courseName) {
        String message = "A New Lecture \"" + lectureTitle + "\" has been added to the course: " + courseName + ".";
        NotificationHelper.showNotification(context, "New Lecture Added", message);
    }


    private static void showNotification(Context context, String title, String message) {
        String channelId = "default_channel_id";
        String channelName = "DefaultChannel";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logopng)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(1, builder.build());
    }
}
