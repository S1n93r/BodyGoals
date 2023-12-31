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
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}