package com.slinger.bodygoals.model;

public class CalendarWeekIdentifier {

    private final int week;
    private final int year;

    private CalendarWeekIdentifier(int week, int year) {
        this.week = week;
        this.year = year;
    }

    public static CalendarWeekIdentifier of(int week, int year) {
        return new CalendarWeekIdentifier(week, year);
    }

    public int getWeek() {
        return week;
    }

    public int getYear() {
        return year;
    }
}