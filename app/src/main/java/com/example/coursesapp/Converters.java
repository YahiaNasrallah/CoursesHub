package com.example.coursesapp;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {


    @TypeConverter
    public static String fromArrayList(ArrayList<Long> list) {
        if (list == null) return null;
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static ArrayList<Long> toArrayList(String value) {
        if (value == null || value.isEmpty()) return new ArrayList<>();
        Type listType = new TypeToken<ArrayList<Long>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }


}
