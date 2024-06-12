package com.slinger.bodygoals.model;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class Goal {

    @Getter
    private final GoalIdentifier goalIdentifier;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private int frequency;

    @Setter
    private List<MuscleGroup> muscleGroups = new ArrayList<>();

    @Setter
    @Getter
    private LocalDate startingDate;

    public Goal(GoalIdentifier goalIdentifier, String name, int frequency, LocalDate startingDate) {

        this.goalIdentifier = goalIdentifier;

        this.name = name;
        this.frequency = frequency;
        this.startingDate = startingDate;
    }

    public static Goal of(GoalIdentifier goalIdentifier, String name, int frequency, LocalDate creationDate) {
        return new Goal(goalIdentifier, name, frequency, creationDate);
    }

    public void addMuscleGroup(MuscleGroup muscleGroup) {
        muscleGroups.add(muscleGroup);
    }

    @Override
    public boolean equals(@Nullable Object o) {

        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        return goalIdentifier.equals(((Goal) o).getGoalIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public List<MuscleGroup> getMuscleGroupsCopy() {
        return Collections.unmodifiableList(muscleGroups);
    }
}