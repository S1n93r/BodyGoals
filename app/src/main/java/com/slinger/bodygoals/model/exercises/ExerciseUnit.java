package com.slinger.bodygoals.model.exercises;

public enum ExerciseUnit {

    KG,
    LBS;

    public String getName() {
        return name().replaceAll("_", " ").toLowerCase();
    }
}