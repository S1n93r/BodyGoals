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

    @Test
    public void sessionLogReturnsCorrectNumberOfSessionsLoggedPerGoal() {

        /* Given */
        SessionLog sessionLog = new SessionLog();

        Calendar calendar = Calendar.getInstance();

        CalendarWeek calendarWeek = CalendarWeek.from(calendar.getTime());

        Goal push = Goal.of("Push", 2);
        Goal pull = Goal.of("Pull", 2);
        Goal legs = Goal.of("Legs", 2);

        /* When */
        sessionLog.logSession(new Session(push, calendar.getTime()));
        sessionLog.logSession(new Session(pull, calendar.getTime()));
        sessionLog.logSession(new Session(legs, calendar.getTime()));
        sessionLog.logSession(new Session(push, calendar.getTime()));
        sessionLog.logSession(new Session(pull, calendar.getTime()));

        /* Then */
        assertEquals(2, sessionLog.getSessionsLogged(calendarWeek, push));
        assertEquals(2, sessionLog.getSessionsLogged(calendarWeek, pull));
        assertEquals(1, sessionLog.getSessionsLogged(calendarWeek, legs));
    }

    @Test
    public void sessionLogReturnsCorrectGoalProgress() {

        /* Given */
        SessionLog sessionLog = new SessionLog();

        Calendar calendar = Calendar.getInstance();

        CalendarWeek calendarWeek = CalendarWeek.from(calendar.getTime());

        Goal push = Goal.of("Push", 3);
        Goal pull = Goal.of("Pull", 2);
        Goal legs = Goal.of("Legs", 2);

        /* When */
        sessionLog.logSession(new Session(push, calendar.getTime()));
        sessionLog.logSession(new Session(pull, calendar.getTime()));
        sessionLog.logSession(new Session(legs, calendar.getTime()));
        sessionLog.logSession(new Session(push, calendar.getTime()));
        sessionLog.logSession(new Session(pull, calendar.getTime()));

        /* Then */
        assertEquals(67, sessionLog.getGoalProgress(calendarWeek, push));
        assertEquals(100, sessionLog.getGoalProgress(calendarWeek, pull));
        assertEquals(50, sessionLog.getGoalProgress(calendarWeek, legs));
    }
}