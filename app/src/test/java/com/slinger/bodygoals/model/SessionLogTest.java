package com.slinger.bodygoals.model;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class SessionLogTest {

    @Test
    public void addingSessionsResultsInCorrectListSizes() {

        /* Given */
        SessionLog sessionLog = new SessionLog();

        Calendar calendar = Calendar.getInstance();

        CalendarWeek calendarWeek = CalendarWeek.from(calendar.getTime());

        /* When */
        sessionLog.logSession(new Session(Goal.of("Push", 2), calendar.getTime()));
        sessionLog.logSession(new Session(Goal.of("Pull", 2), calendar.getTime()));
        sessionLog.logSession(new Session(Goal.of("Legs", 2), calendar.getTime()));

        /* Then */
        assertEquals(1, sessionLog.getLoggedWeeks().size());
        assertEquals(3, sessionLog.getSessionsCopy(calendarWeek).size());
    }
}