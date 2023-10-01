package com.slinger.bodygoals.model;

import java.util.ArrayList;
import java.util.List;

public class Goal {

    private final String name;
    private final int frequency;
    private final List<MuscleGroup> muscleGroups = new ArrayList<>();

    private Goal(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public void addMuscleGroup(MuscleGroup muscleGroup) {
        muscleGroups.add(muscleGroup);
    }

    public static Goal of(String name, int frequency) {
        return new Goal(name, frequency);
    }

    public String getName() {
        return name;
    }

    public int getFrequency() {
        return frequency;
    }
}