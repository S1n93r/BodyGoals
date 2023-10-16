package com.slinger.bodygoals.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Calendar;
import java.util.Map;

public class SessionLogTest {

    @Test
    public void addingSessionsResultsInCorrectListSizes() {

        /* Given */
        SessionLog sessionLog = new SessionLog();

        Calendar calendar = Calendar.getInstance();

        CalendarWeek calendarWeek = CalendarWeek.from(calendar.getTime());

        /* When */
        sessionLog.logSession(new Session(Goal.of("Push", 2, calendarWeek), calendar.getTime()));
        sessionLog.logSession(new Session(Goal.of("Pull", 2, calendarWeek), calendar.getTime()));
        sessionLog.logSession(new Session(Goal.of("Legs", 2, calendarWeek), calendar.getTime()));

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

        Goal push = Goal.of("Push", 2, calendarWeek);
        Goal pull = Goal.of("Pull", 2, calendarWeek);
        Goal legs = Goal.of("Legs", 2, calendarWeek);

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

        Goal push = Goal.of("Push", 3, calendarWeek);
        Goal pull = Goal.of("Pull", 2, calendarWeek);
        Goal legs = Goal.of("Legs", 2, calendarWeek);

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

    @Test
    public void coverageCorrectlyRepresentsAddedGoalsAndSessions() {

        /* Given */
        SessionLog sut = new SessionLog();

        Calendar calendar = Calendar.getInstance();

        CalendarWeek calendarWeek = CalendarWeek.from(calendar.getTime());

        Goal push = Goal.of("Push", 3, calendarWeek);
        push.addMuscleGroup(MuscleGroup.CHEST);
        push.addMuscleGroup(MuscleGroup.TRICEPS);

        Goal pull = Goal.of("Pull", 2, calendarWeek);
        pull.addMuscleGroup(MuscleGroup.LATS);
        pull.addMuscleGroup(MuscleGroup.BICEPS);

        Goal legs = Goal.of("Legs", 2, calendarWeek);
        legs.addMuscleGroup(MuscleGroup.QUADS);
        legs.addMuscleGroup(MuscleGroup.HARM_STRINGS);
        legs.addMuscleGroup(MuscleGroup.CALVES);

        sut.logSession(new Session(pull, calendar.getTime()));
        sut.logSession(new Session(push, calendar.getTime()));
        sut.logSession(new Session(pull, calendar.getTime()));
        sut.logSession(new Session(legs, calendar.getTime()));
        sut.logSession(new Session(push, calendar.getTime()));
        sut.logSession(new Session(pull, calendar.getTime()));

        /* When */
        Map<MuscleGroup, Progress> progressPerMuscleGroup = sut.progressPerMuscleGroup(calendarWeek);

        /* Then */
        assertEquals(3, progressPerMuscleGroup.get(MuscleGroup.CHEST).getMax());
        assertEquals(2, progressPerMuscleGroup.get(MuscleGroup.CHEST).getCurrent());
        assertEquals(67, progressPerMuscleGroup.get(MuscleGroup.CHEST).getCurrentPercent());

        assertEquals(3, progressPerMuscleGroup.get(MuscleGroup.TRICEPS).getMax());
        assertEquals(2, progressPerMuscleGroup.get(MuscleGroup.TRICEPS).getCurrent());
        assertEquals(67, progressPerMuscleGroup.get(MuscleGroup.TRICEPS).getCurrentPercent());

        assertEquals(3, progressPerMuscleGroup.get(MuscleGroup.LATS).getMax());
        assertEquals(3, progressPerMuscleGroup.get(MuscleGroup.LATS).getCurrent());
        assertEquals(100, progressPerMuscleGroup.get(MuscleGroup.LATS).getCurrentPercent());

        assertEquals(3, progressPerMuscleGroup.get(MuscleGroup.BICEPS).getMax());
        assertEquals(3, progressPerMuscleGroup.get(MuscleGroup.BICEPS).getCurrent());
        assertEquals(100, progressPerMuscleGroup.get(MuscleGroup.BICEPS).getCurrentPercent());

        assertEquals(3, progressPerMuscleGroup.get(MuscleGroup.QUADS).getMax());
        assertEquals(1, progressPerMuscleGroup.get(MuscleGroup.QUADS).getCurrent());
        assertEquals(33, progressPerMuscleGroup.get(MuscleGroup.QUADS).getCurrentPercent());

        assertEquals(3, progressPerMuscleGroup.get(MuscleGroup.HARM_STRINGS).getMax());
        assertEquals(1, progressPerMuscleGroup.get(MuscleGroup.HARM_STRINGS).getCurrent());
        assertEquals(33, progressPerMuscleGroup.get(MuscleGroup.HARM_STRINGS).getCurrentPercent());

        assertEquals(3, progressPerMuscleGroup.get(MuscleGroup.CALVES).getMax());
        assertEquals(1, progressPerMuscleGroup.get(MuscleGroup.CALVES).getCurrent());
        assertEquals(33, progressPerMuscleGroup.get(MuscleGroup.CALVES).getCurrentPercent());
    }
}