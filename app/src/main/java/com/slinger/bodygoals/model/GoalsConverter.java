package com.slinger.bodygoals.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.room.TypeConverter;

public class GoalsConverter {

    @TypeConverter
    public static List<Goal> fromString(String value) {
        Type type = new TypeToken<ArrayList<Goal>>() {
        }.getType();

        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromArrayList(List<Goal> goals) {

        Gson gson = new Gson();

        return gson.toJson(goals);
    }
}
