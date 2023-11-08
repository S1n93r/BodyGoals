package com.slinger.bodygoals.model;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import com.slinger.bodygoals.ui.dtos.Identifieable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Goal implements Identifieable {

    public static Goal EMPTY = new Goal(GoalIdentifier.of(-1), "", 0, LocalDate.now());

    private final GoalIdentifier goalIdentifier;

    private String name;
    private int frequency;
    private List<MuscleGroup> muscleGroups = new ArrayList<>();

    /* FIXME: Conversion doesn't work */
    @ColumnInfo(name = "starting_date")
    @TypeConverters({LocalDateConverter.class})
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
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

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    /* TODO: Unused. */
    public int getCreationWeek() {
        return DateUtil.getWeekOfYear(startingDate);
    }

    public void setMuscleGroups(List<MuscleGroup> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }

    public GoalIdentifier getGoalIdentifier() {
        return goalIdentifier;
    }

    @Override
    public Identifier getIdentifier() {
        return goalIdentifier;
    }
}