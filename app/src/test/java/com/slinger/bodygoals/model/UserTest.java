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
        sut.addGoal(new Goal(GoalIdentifier.of(1), "Push / Pull", 2, today));
        sut.addGoal(new Goal(GoalIdentifier.of(2), "Shoulders / Abs", 2, today));
        sut.addGoal(new Goal(GoalIdentifier.of(3), "Legs", 2, today));

        /* Then */
        assertEquals(3, sut.getGoals().size());

        assertEquals("Push / Pull", sut.getGoals().get(0).getName());
        assertEquals(2, sut.getGoals().get(1).getFrequency());
        assertEquals(today, sut.getGoals().get(2).getCreationDate());
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

        sut.editGoal(GoalIdentifier.of(1), "Push / Pull", 3,
                Lists.of(MuscleGroup.BICEPS, MuscleGroup.LATS, MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.FOREARMS));

        /* Then */
        Goal actualGoal = sut.getGoals().get(0);

        assertEquals("Push / Pull", actualGoal.getName());
        assertEquals(3, actualGoal.getFrequency());
        assertEquals(Lists.of(MuscleGroup.BICEPS, MuscleGroup.LATS, MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.FOREARMS),
                actualGoal.getMuscleGroupsCopy());
    }
}