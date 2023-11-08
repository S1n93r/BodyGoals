package com.slinger.bodygoals.model;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @TypeConverter
    public static LocalDate fromString(String value) {
        return LocalDate.parse(value, FORMATTER);
    }

    @TypeConverter
    public static String fromLocalDate(LocalDate localDate) {
        return localDate.format(FORMATTER);
    }
}
