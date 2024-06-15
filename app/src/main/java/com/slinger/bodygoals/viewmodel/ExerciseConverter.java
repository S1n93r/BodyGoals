package com.slinger.bodygoals.viewmodel;

import com.slinger.bodygoals.model.exercises.Exercise;
import com.slinger.bodygoals.ui.fragments.exercises.ExerciseDto;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ExerciseConverter {

    public static Exercise from(ExerciseDto exerciseDto) {
        return Exercise.of(exerciseDto.getType(), exerciseDto.getVariant(), exerciseDto.getUnit(), exerciseDto.getRepGoal());
    }

    public static ExerciseDto to(Exercise exercise) {

        return ExerciseDto.of(
                exercise.getExerciseIdentifier(),
                exercise.getExerciseIdentifier().getExerciseType(),
                exercise.getVariant(),
                exercise.getUnit(),
                exercise.getRepGoal(),
                exercise.getProgressHistoryStream().toList(),
                exercise.getTrend()
        );
    }
}