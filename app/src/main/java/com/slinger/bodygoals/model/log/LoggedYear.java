package com.slinger.bodygoals.model.log;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

public class LoggedYear {

    public final int year;

    @ColumnInfo(name = "logged_weeks")
    @TypeConverters({LoggedWeekListConverter.class})
    private List<LoggedWeek> loggedSessions = new ArrayList<>();

    public LoggedYear(int year) {
        this.year = year;
    }
}