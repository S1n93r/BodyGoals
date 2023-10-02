package com.slinger.bodygoals.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import androidx.room.TypeConverter;

public class SessionLogConverter {

    @TypeConverter
    public static SessionLog fromString(String value) {
        Type type = new TypeToken<SessionLog>() {
        }.getType();

        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromArrayList(SessionLog sessionLog) {

        Gson gson = new Gson();

        return gson.toJson(sessionLog);
    }
}
