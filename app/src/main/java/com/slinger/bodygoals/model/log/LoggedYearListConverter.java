package com.slinger.bodygoals.model.log;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class LoggedYearListConverter {

    @TypeConverter
    public static List<LoggedYear> fromString(String value) {
        Type type = new TypeToken<List<LoggedYear>>() {
        }.getType();

        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromArrayList(List<LoggedYear> loggedYears) {

        Gson gson = new Gson();

        return gson.toJson(loggedYears);
    }
}
