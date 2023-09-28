package com.slinger.bodygoals.model;

public class CalendarWeekDay<T> {

    private final CalendarWeekIdentifier calendarWeekIdentifier;

    private final String dayName;

    private final T loggedSubject;

    private CalendarWeekDay(CalendarWeekIdentifier calendarWeekIdentifier, String dayName, T loggedSubject) {
        this.calendarWeekIdentifier = calendarWeekIdentifier;
        this.dayName = dayName;
        this.loggedSubject = loggedSubject;
    }

    public static <T> CalendarWeekDay<T> of(CalendarWeekIdentifier calendarWeekIdentifier, String day, T subject) {
        return new CalendarWeekDay<T>(calendarWeekIdentifier, day, subject);
    }

    public String getDayName() {
        return dayName;
    }

    public T getLoggedSubject() {
        return loggedSubject;
    }

    public CalendarWeekIdentifier getCalendarWeekIdentifier() {
        return calendarWeekIdentifier;
    }
}