package com.slinger.bodygoals.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalendarWeek<T> {

    private final CalendarWeekIdentifier calendarWeekIdentifier;

    private final List<CalendarWeekDay<T>> loggedDays = new ArrayList<>();

    public CalendarWeek(CalendarWeekIdentifier calendarWeekIdentifier) {
        this.calendarWeekIdentifier = calendarWeekIdentifier;
    }

    public void logDay(CalendarWeekDay<T> day) {
        loggedDays.add(day);
    }

    public List<CalendarWeekDay<T>> getLoggedDaysCopy() {
        return Collections.unmodifiableList(loggedDays);
    }

    public int getDaysSpend(Class<? extends T> subClass) {

        List<CalendarWeekDay<T>> matchingDays = new ArrayList<>();

        for (CalendarWeekDay<T> loggedDay : loggedDays) {
            if (loggedDay.getLoggedSubject().getClass() == subClass)
                matchingDays.add(loggedDay);
        }

        return matchingDays.size();
    }

    public CalendarWeekIdentifier getCalendarWeekIdentifier() {
        return calendarWeekIdentifier;
    }
}