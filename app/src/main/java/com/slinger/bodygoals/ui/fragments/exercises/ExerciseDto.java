package com.slinger.bodygoals.ui.fragments.exercises;

import com.slinger.bodygoals.model.exercises.ExerciseIdentifier;
import com.slinger.bodygoals.model.exercises.ExerciseType;
import com.slinger.bodygoals.model.exercises.ExerciseUnit;

import java.util.List;

import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;
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

    List<Double> progressHistory;

    double trend;

    public Stream<Double> getProgressHistory() {
        return StreamSupport.stream(progressHistory);
    }
}