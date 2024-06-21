package com.slinger.bodygoals.model;

public enum MuscleGroup {

    ABS,
    BICEPS,
    CALVES,
    CHEST,
    FOREARMS,
    HARM_STRINGS,
    LATS,
    LOWER_BACK,
    NECK,
    QUADS,
    SHOULDERS,
    TRICEPS;

    public String getName() {
        return name().replaceAll("_", " ").toLowerCase();
    }
}