package com.slinger.bodygoals.model.log;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class LoggedWeekListConverter {

    @TypeConverter
    public static List<LoggedWeek> fromString(String value) {
        Type type = new TypeToken<List<LoggedWeek>>() {
        }.getType();

        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromArrayList(List<LoggedWeek> loggedWeeks) {

        Gson gson = new Gson();

        return gson.toJson(loggedWeeks);
    }
}
