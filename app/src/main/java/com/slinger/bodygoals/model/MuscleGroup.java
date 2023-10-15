package com.slinger.bodygoals.model;

public enum MuscleGroup {

    NECK,
    CHEST,
    LATS,
    SHOULDERS,
    BICEPS,
    TRICEPS,
    FOREARMS,
    ABS,
    LOWER_BACK,
    QUADS,
    HARM_STRINGS,
    CALVES;

    public String getName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}