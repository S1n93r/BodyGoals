package com.slinger.bodygoals.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Goal implements Comparable<Goal> {

    private final String name;
    private final int frequency;
    private final List<MuscleGroup> muscleGroups = new ArrayList<>();

    private final CalendarWeek creationWeek;

    private Goal(String name, int frequency, CalendarWeek creationWeek) {
        this.name = name;
        this.frequency = frequency;
        this.creationWeek = creationWeek;
    }

    public void addMuscleGroup(MuscleGroup muscleGroup) {
        muscleGroups.add(muscleGroup);
    }

    public static Goal of(String name, int frequency, CalendarWeek calendarWeek) {
        return new Goal(name, frequency, calendarWeek);
    }

    public String getName() {
        return name;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj == this)
            return true;

        if (!(obj instanceof Goal))
            return false;

        return name.equals(((Goal) obj).getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Goal o) {
        return name.toLowerCase().compareTo(o.name.toLowerCase());
    }

    public List<MuscleGroup> getMuscleGroupsCopy() {
        return Collections.unmodifiableList(muscleGroups);
    }

    public CalendarWeek getCreationWeek() {
        return creationWeek;
    }
}