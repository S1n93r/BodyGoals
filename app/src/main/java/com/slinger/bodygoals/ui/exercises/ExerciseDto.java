package com.slinger.bodygoals.ui.exercises;

import com.slinger.bodygoals.model.exercises.Exercise;
import com.slinger.bodygoals.model.exercises.ExerciseIdentifier;
import com.slinger.bodygoals.model.exercises.ExerciseType;
import com.slinger.bodygoals.model.exercises.ExerciseUnit;

import java8.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class ExerciseDto {

    ExerciseIdentifier exerciseIdentifier;

    ExerciseType type;

    String variant;

    ExerciseUnit unit;

    int repGoal;

    Stream<Double> progressHistory;

    public static ExerciseDto from(Exercise exercise) {

        return ExerciseDto.of(
                exercise.getExerciseIdentifier(),
                exercise.getExerciseIdentifier().getExerciseType(),
                exercise.getVariant(),
                exercise.getUnit(),
                exercise.getRepGoal(),
                exercise.getProgressHistoryStream()
        );
    }
}