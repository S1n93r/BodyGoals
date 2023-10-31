package com.slinger.bodygoals.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import java8.util.Lists;

public class UserTest {

    @Test
    public void addingGoalsWorks() {

        /* Given */
        User sut = new User();

        Date today = Calendar.getInstance().getTime();

        /* When */
        sut.addGoalWithNewId("Push / Pull", 2, today, Lists.of());
        sut.addGoalWithNewId("Shoulders / Abs", 2, today, Lists.of());
        sut.addGoalWithNewId("Legs", 2, today, Lists.of());

        /* Then */
        assertEquals(3, sut.getGoals().size());

        assertEquals("Push / Pull", sut.getGoals().get(0).getName());
        assertEquals(2, sut.getGoals().get(1).getFrequency());
        assertEquals(today, sut.getGoals().get(2).getStartingDate());
    }

    @Test
    public void addingFreshGoalsWorks() {

        /* Given */
        User sut = new User();

        Date today = Calendar.getInstance().getTime();

        /* When */
        sut.addGoalWithNewId("Push / Pull", 2, today,
                Lists.of(MuscleGroup.BICEPS, MuscleGroup.LATS, MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.FOREARMS));
        sut.addGoalWithNewId("Shoulders / Abs", 2, today, Lists.of(MuscleGroup.SHOULDERS, MuscleGroup.NECK, MuscleGroup.ABS));
        sut.addGoalWithNewId("Legs", 2, today, Lists.of(MuscleGroup.QUADS, MuscleGroup.HARM_STRINGS, MuscleGroup.CALVES));

        /* Then */
        assertEquals(3, sut.getGoals().size());

        assertEquals(1, sut.getGoals().get(0).getGoalIdentifier().getId());
        assertEquals(2, sut.getGoals().get(1).getGoalIdentifier().getId());
        assertEquals(3, sut.getGoals().get(2).getGoalIdentifier().getId());

        assertEquals(Lists.of(MuscleGroup.SHOULDERS, MuscleGroup.NECK, MuscleGroup.ABS), sut.getGoals().get(1).getMuscleGroupsCopy());
    }

    @Test
    public void editingGoalChangesItCorrectly() {

        /* Given */
        User sut = new User();

        Date today = Calendar.getInstance().getTime();

        /* When */
        sut.addGoalWithNewId("push pull", 2, today,
                Lists.of(MuscleGroup.BICEPS, MuscleGroup.LATS, MuscleGroup.TRICEPS, MuscleGroup.CHEST));
        sut.addGoalWithNewId("Legs", 2, today,
                Lists.of(MuscleGroup.QUADS, MuscleGroup.CALVES, MuscleGroup.HARM_STRINGS, MuscleGroup.LOWER_BACK));
        sut.addGoalWithNewId("Shoulders / Abs", 2, today,
                Lists.of(MuscleGroup.SHOULDERS, MuscleGroup.ABS));

        sut.editGoal(GoalIdentifier.of(1), "Push / Pull", 3, today,
                Lists.of(MuscleGroup.BICEPS, MuscleGroup.LATS, MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.FOREARMS));

        /* Then */
        Goal actualGoalPushPull = sut.getGoals().get(0);
        Goal actualGoalLegs = sut.getGoals().get(1);
        Goal actualGoalShoulderAbs = sut.getGoals().get(2);

        assertEquals("Push / Pull", actualGoalPushPull.getName());
        assertEquals(3, actualGoalPushPull.getFrequency());
        assertEquals(Lists.of(MuscleGroup.BICEPS, MuscleGroup.LATS, MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.FOREARMS),
                actualGoalPushPull.getMuscleGroupsCopy());

        assertEquals("Legs", actualGoalLegs.getName());
        assertEquals(2, actualGoalLegs.getFrequency());
        assertEquals(Lists.of(MuscleGroup.QUADS, MuscleGroup.CALVES, MuscleGroup.HARM_STRINGS, MuscleGroup.LOWER_BACK),
                actualGoalLegs.getMuscleGroupsCopy());

        assertEquals("Shoulders / Abs", actualGoalShoulderAbs.getName());
        assertEquals(2, actualGoalShoulderAbs.getFrequency());
        assertEquals(Lists.of(MuscleGroup.SHOULDERS, MuscleGroup.ABS),
                actualGoalShoulderAbs.getMuscleGroupsCopy());
    }
}