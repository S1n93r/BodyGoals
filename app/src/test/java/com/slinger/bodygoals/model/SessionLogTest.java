package com.slinger.bodygoals.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class SessionLogTest {

    @Test
    public void addingSessionsResultsInCorrectListSizes() {

        /* Given */
        SessionLog sessionLog = new SessionLog();

        Calendar calendar = Calendar.getInstance();

        Date date = calendar.getTime();

        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        /* When */
        sessionLog.logSession(new Session(Goal.of("Push", 2, date), calendar.getTime()));
        sessionLog.logSession(new Session(Goal.of("Pull", 2, date), calendar.getTime()));
        sessionLog.logSession(new Session(Goal.of("Legs", 2, date), calendar.getTime()));

        /* Then */
        assertEquals(1, sessionLog.getLoggedWeeks().size());
        assertEquals(3, sessionLog.getSessionsCopy(weekOfYear).size());
    }

    @Test
    public void sessionLogReturnsCorrectNumberOfSessionsLoggedPerGoal() {

        /* Given */
        SessionLog sessionLog = new SessionLog();

        Calendar calendar = Calendar.getInstance();

        Date date = calendar.getTime();

        Goal push = Goal.of("Push", 2, date);
        Goal pull = Goal.of("Pull", 2, date);
        Goal legs = Goal.of("Legs", 2, date);

        /* When */
        sessionLog.logSession(new Session(push, calendar.getTime()));
        sessionLog.logSession(new Session(pull, calendar.getTime()));
        sessionLog.logSession(new Session(legs, calendar.getTime()));
        sessionLog.logSession(new Session(push, calendar.getTime()));
        sessionLog.logSession(new Session(pull, calendar.getTime()));

        /* Then */
        assertEquals(2, sessionLog.getSessionsLogged(date, push));
        assertEquals(2, sessionLog.getSessionsLogged(date, pull));
        assertEquals(1, sessionLog.getSessionsLogged(date, legs));
    }

    @Test
    public void sessionLogReturnsCorrectGoalProgress() {

        /* Given */
        SessionLog sessionLog = new SessionLog();

        Calendar calendar = Calendar.getInstance();

        Date date = calendar.getTime();

        Goal push = Goal.of("Push", 3, date);
        Goal pull = Goal.of("Pull", 2, date);
        Goal legs = Goal.of("Legs", 2, date);

        /* When */
        sessionLog.logSession(new Session(push, date));
        sessionLog.logSession(new Session(pull, date));
        sessionLog.logSession(new Session(legs, date));
        sessionLog.logSession(new Session(push, date));
        sessionLog.logSession(new Session(pull, date));

        /* Then */
        assertEquals(67, sessionLog.getGoalProgress(date, push));
        assertEquals(100, sessionLog.getGoalProgress(date, pull));
        assertEquals(50, sessionLog.getGoalProgress(date, legs));
    }

    @Test
    public void coverageCorrectlyRepresentsAddedGoalsAndSessions() {

        /* Given */
        SessionLog sut = new SessionLog();

        Calendar calendar = Calendar.getInstance();

        Date date = calendar.getTime();

        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        Goal push = Goal.of("Push", 3, date);
        push.addMuscleGroup(MuscleGroup.CHEST);
        push.addMuscleGroup(MuscleGroup.TRICEPS);

        Goal pull = Goal.of("Pull", 2, date);
        pull.addMuscleGroup(MuscleGroup.LATS);
        pull.addMuscleGroup(MuscleGroup.BICEPS);

        Goal legs = Goal.of("Legs", 2, date);
        legs.addMuscleGroup(MuscleGroup.QUADS);
        legs.addMuscleGroup(MuscleGroup.HARM_STRINGS);
        legs.addMuscleGroup(MuscleGroup.CALVES);

        sut.logSession(new Session(pull, date));
        sut.logSession(new Session(push, date));
        sut.logSession(new Session(pull, date));
        sut.logSession(new Session(legs, date));
        sut.logSession(new Session(push, date));
        sut.logSession(new Session(pull, date));

        /* When */
        Map<MuscleGroup, Progress> progressPerMuscleGroup = sut.progressPerMuscleGroup(weekOfYear);

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