package com.slinger.bodygoals.model;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

public class CalendarWeek {

    private final int week;
    private final int year;

    private CalendarWeek(int week, int year) {
        this.week = week;
        this.year = year;
    }

    public static CalendarWeek of(int week, int year) {
        return new CalendarWeek(week, year);
    }

    public static CalendarWeek from(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return CalendarWeek.of(calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR));
    }

    public static CalendarWeek previousWeek(CalendarWeek calendarWeek) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, calendarWeek.getWeek());
        calendar.set(Calendar.YEAR, calendarWeek.getYear());

        calendar.add(Calendar.WEEK_OF_YEAR, -1);

        return CalendarWeek.from(calendar.getTime());
    }

    public static CalendarWeek nextWeek(CalendarWeek calendarWeek) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, calendarWeek.getWeek());
        calendar.set(Calendar.YEAR, calendarWeek.getYear());

        calendar.add(Calendar.WEEK_OF_YEAR, 1);

        return CalendarWeek.from(calendar.getTime());
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

        if (!(obj instanceof CalendarWeek))
            return false;

        CalendarWeek calendarWeek = (CalendarWeek) obj;

        return calendarWeek.getWeek() == getWeek() && calendarWeek.getYear() == getYear();
    }
}