package com.slinger.bodygoals.model.log;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

public class LoggedWeek {

    public final int year;

    @ColumnInfo(name = "logged_sessions")
    @TypeConverters({SessionListConverter.class})
    private List<Session> loggedSessions = new ArrayList<>();

    public LoggedWeek(int year) {
        this.year = year;
    }
}