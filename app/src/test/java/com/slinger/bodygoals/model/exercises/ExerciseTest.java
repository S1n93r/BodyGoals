package com.slinger.bodygoals.model.exercises;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.slinger.bodygoals.model.MuscleGroup;

import org.junit.Test;

import java.util.List;

import java8.util.Lists;

public class ExerciseTest {

    @Test
    public void creatingExerciseViaConstructorSetsAllValuesCorrectly() {

        Exercise sut = Exercise.of(ExerciseType.CHEST_PRESS, "dumbbells", ExerciseUnit.KG, 10);

        List<MuscleGroup> expectedMuscleGroups = ExerciseType.CHEST_PRESS.getMuscleGroupsStream().toList();
        List<MuscleGroup> actualMuscleGroups = sut.getMuscleGroupsStream().toList();

        assertEquals(expectedMuscleGroups.get(0), actualMuscleGroups.get(0));
        assertEquals(expectedMuscleGroups.get(1), actualMuscleGroups.get(1));

        assertEquals(ExerciseIdentifier.of(ExerciseType.CHEST_PRESS, "dumbbells"), sut.getExerciseIdentifier());
        assertEquals("dumbbells", sut.getVariant());
        assertEquals(ExerciseUnit.KG, sut.getUnit());
        assertEquals(10, sut.getRepGoal());
    }

    @Test
    public void addingProgressChangesHistoryAccordingly() {

        Exercise sut = Exercise.of(ExerciseType.CHEST_PRESS, "dumbbells", ExerciseUnit.KG, 10);

        sut.addProgress(40);
        sut.addProgress(45);
        sut.addProgress(50);

        assertEquals(Lists.of(40.0, 45.0, 50.0), sut.getProgressHistoryStream().toList());
    }

    @Test
    public void noTrendCalculatedCorrectly() {

        Exercise sut = Exercise.of(ExerciseType.CHEST_PRESS, "dumbbells", ExerciseUnit.KG, 10);

        assertEquals(0, sut.getTrend());
    }

    @Test
    public void maxTrendCalculatedCorrectly() {

        Exercise sut = Exercise.of(ExerciseType.CHEST_PRESS, "dumbbells", ExerciseUnit.KG, 10);

        sut.addProgress(60);

        assertEquals(100, sut.getTrend());
    }

    @Test
    public void positiveTrendCalculatedCorrectly() {

        Exercise sut = Exercise.of(ExerciseType.CHEST_PRESS, "dumbbells", ExerciseUnit.KG, 10);

        sut.addProgress(60);
        sut.addProgress(70);

        assertEquals(16.67, sut.getTrend());
    }

    @Test
    public void negativeTrendCalculatedCorrectly() {

        Exercise sut = Exercise.of(ExerciseType.CHEST_PRESS, "dumbbells", ExerciseUnit.KG, 10);

        sut.addProgress(70);
        sut.addProgress(60);

        assertEquals(-14.29, sut.getTrend());
    }
}