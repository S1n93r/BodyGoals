package com.slinger.bodygoals.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

public class SessionListConverter {

    @TypeConverter
    public static List<Session> fromString(String value) {
        Type type = new TypeToken<List<Session>>() {
        }.getType();

        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromArrayList(List<Session> sessions) {

        Gson gson = new Gson();

        return gson.toJson(sessions);
    }
}
