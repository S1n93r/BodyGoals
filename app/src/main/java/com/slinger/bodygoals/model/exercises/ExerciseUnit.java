package com.slinger.bodygoals.model.exercises;

public enum ExerciseUnit {

    KG,
    REPS;

    public String getName() {
        return name().replaceAll("_", " ").toLowerCase();
    }

    public static ExerciseUnit fromName(String name) {
        return ExerciseUnit.valueOf(name.toUpperCase().replaceAll(" ", "_"));
    }
}