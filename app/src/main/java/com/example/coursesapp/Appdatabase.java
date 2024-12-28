package com.example.coursesapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {User.class, Category.class, Course.class, Lecture.class, MyCourses.class, BookMarks.class, Notification.class},
        version = 24,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class Appdatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract CourseDao courseDao();
    public abstract LectureDao lectureDao();
    public abstract MyCoursesDao myCoursesDao();
    public abstract BookMarksDao bookMarksDao();
    public abstract NotificationDao notificationDao();

    private static volatile Appdatabase INSTANCE;

    public static Appdatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Appdatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    Appdatabase.class, "AppDataDatabase").allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }



}
