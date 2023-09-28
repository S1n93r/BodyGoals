package com.slinger.bodygoals.model;

import java.util.HashMap;
import java.util.Map;

public class CalendarWeekLog<T> {

    private final Map<CalendarWeekIdentifier, CalendarWeek<T>> calendarWeeksLog = new HashMap<>();

    public void logDay(CalendarWeekDay<T> day) {

        CalendarWeekIdentifier calendarWeekIdentifier = day.getCalendarWeekIdentifier();

        CalendarWeek<T> calendarWeek = calendarWeeksLog.get(calendarWeekIdentifier);

        if (calendarWeek == null) {
            calendarWeek = new CalendarWeek<>(calendarWeekIdentifier);
            calendarWeeksLog.put(calendarWeekIdentifier, calendarWeek);
        }
        
        calendarWeek.logDay(day);
    }
}