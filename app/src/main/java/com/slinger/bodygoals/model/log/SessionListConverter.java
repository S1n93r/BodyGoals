package com.slinger.bodygoals.model.log;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.slinger.bodygoals.model.util.AdapterFactory;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

public class SessionListConverter {

    private static final TypeAdapter<LocalDate> localDateTypeAdapter = AdapterFactory.createLocalDateAdapter();

    @TypeConverter
    public static List<Session> fromString(String value) {

        Type type = new TypeToken<List<Session>>() {
        }.getType();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateTypeAdapter)
                .create();
        
        return gson.fromJson(value, type);
    }

    @TypeConverter
    public static String fromArrayList(List<Session> sessions) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateTypeAdapter)
                .create();

        return gson.toJson(sessions);
    }
}
