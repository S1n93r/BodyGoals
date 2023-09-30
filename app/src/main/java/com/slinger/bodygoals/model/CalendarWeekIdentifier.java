package com.slinger.bodygoals.model;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

public class CalendarWeekIdentifier {

    private static final String DAY_OF_WEEK = "EEEE";
    private static final String DAY = "dd";
    private static final String MONTH_TEXT = "MMM";
    private static final String MONTH = "MM";
    private static final String YEAR = "yyyy";

    private final int week;
    private final int year;

    private CalendarWeekIdentifier(int week, int year) {
        this.week = week;
        this.year = year;
    }

    public static CalendarWeekIdentifier of(int week, int year) {
        return new CalendarWeekIdentifier(week, year);
    }

    public static CalendarWeekIdentifier from(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.get(Calendar.DAY_OF_MONTH);

        return CalendarWeekIdentifier.of(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
    }

    public int getWeek() {
        return week;
    }

    public int getYear() {
        return year;
    }

    @Override
    public int hashCode() {
        return week + year;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj == this)
            return true;

        if (!(obj instanceof CalendarWeekIdentifier))
            return false;

        CalendarWeekIdentifier calendarWeekIdentifier = (CalendarWeekIdentifier) obj;

        return calendarWeekIdentifier.getWeek() == getWeek() && calendarWeekIdentifier.getYear() == getYear();
    }
}