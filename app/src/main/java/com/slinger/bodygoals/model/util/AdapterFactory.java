package com.slinger.bodygoals.model.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

public final class AdapterFactory {

    private AdapterFactory() {
        /* Util */
    }

    public static TypeAdapter<LocalDate> createLocalDateAdapter() {

        return new TypeAdapter<LocalDate>() {
            @Override
            public void write(JsonWriter out, LocalDate value) throws IOException {
                out.value(value.toEpochDay());
            }

            @Override
            public LocalDate read(JsonReader in) throws IOException {
                return LocalDate.ofEpochDay(in.nextLong());
            }
        };
    }
}