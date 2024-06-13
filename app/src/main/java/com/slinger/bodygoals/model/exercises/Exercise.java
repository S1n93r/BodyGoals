package com.slinger.bodygoals.model.exercises;

import com.slinger.bodygoals.model.MuscleGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import lombok.Getter;

public class Exercise {

    @Getter
    private final ExerciseIdentifier exerciseIdentifier;

    private final ExerciseType type;

    @Getter
    private final String variant;

    @Getter
    private final ExerciseUnit unit;

    @Getter
    private final int repGoal;

    private Exercise(ExerciseType type, String variant, ExerciseUnit unit, int repGoal) {

        this.exerciseIdentifier = ExerciseIdentifier.of(type, variant);
        this.type = type;
        this.variant = variant;
        this.unit = unit;
        this.repGoal = repGoal;
    }

    public static Exercise of(ExerciseType type, String variant, ExerciseUnit unit, int repGoal) {
        return new Exercise(type, variant, unit, repGoal);
    }

    private final List<Double> progressHistory = new ArrayList<>();

    public Stream<MuscleGroup> getMuscleGroupsStream() {
        return type.getMuscleGroupsStream();
    }

    public void addProgress(double progress) {
        progressHistory.add(progress);
    }

    public Stream<Double> getProgressHistoryStream() {
        return progressHistory.stream();
    }
}