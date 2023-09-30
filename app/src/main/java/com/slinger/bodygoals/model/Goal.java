package com.slinger.bodygoals.model;

import java.util.ArrayList;
import java.util.List;

public class Goal {

    private final String name;
    private final int frequency;
    private final List<MuscleGroup> mouscleGroups = new ArrayList<>();

    public Goal(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }
}