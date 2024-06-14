package com.slinger.bodygoals.model.exercises;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExerciseListConverter {

    @TypeConverter
    public static List<Exercise> fromString(String value) {
        Type type = new TypeToken<ArrayList<Exercise>>() {
        }.getType();

        Gson gson = new GsonBuilder().create();

        return gson.fromJson(value, type);
    }

    @TypeConverter
    public static String fromArrayList(List<Exercise> exercises) {

        Gson gson = new GsonBuilder().create();

        return gson.toJson(exercises);
    }
}
