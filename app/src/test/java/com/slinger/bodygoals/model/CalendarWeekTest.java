package com.slinger.bodygoals.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalendarWeekTest {

    @Test
    public void calendarWeekFindsLoggedDayCountByClassGiven() {

        /* Given */
        CalendarWeekIdentifier calendarWeekIdentifier = CalendarWeekIdentifier.of(39, 2023);

        CalendarWeek<Object> calendarWeek = new CalendarWeek<>(calendarWeekIdentifier);

        /* When */
        calendarWeek.logDay(CalendarWeekDay.of(calendarWeekIdentifier, "Monday", 3));
        calendarWeek.logDay(CalendarWeekDay.of(calendarWeekIdentifier, "Tuesday", 3d));
        calendarWeek.logDay(CalendarWeekDay.of(calendarWeekIdentifier, "Wednesday", 3f));
        calendarWeek.logDay(CalendarWeekDay.of(calendarWeekIdentifier, "Thursday", 3));
        calendarWeek.logDay(CalendarWeekDay.of(calendarWeekIdentifier, "Friday", 3d));
        calendarWeek.logDay(CalendarWeekDay.of(calendarWeekIdentifier, "Saturday", 3f));
        calendarWeek.logDay(CalendarWeekDay.of(calendarWeekIdentifier, "Sunday", 3));

        /* Then */
        assertEquals(3, calendarWeek.getDaysSpend(Integer.class));
        assertEquals(2, calendarWeek.getDaysSpend(Double.class));
        assertEquals(2, calendarWeek.getDaysSpend(Float.class));
        assertEquals(0, calendarWeek.getDaysSpend(String.class));
    }
}