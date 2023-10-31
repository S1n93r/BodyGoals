package com.slinger.bodygoals.model;

import androidx.annotation.Nullable;

import com.slinger.bodygoals.ui.dtos.Identifieable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Goal implements Identifieable {

    public static Goal EMPTY = new Goal(GoalIdentifier.of(-1), "", 0, Calendar.getInstance().getTime());

    private final GoalIdentifier goalIdentifier;

    private String name;
    private int frequency;
    private List<MuscleGroup> muscleGroups = new ArrayList<>();

    private Date startingDate;

    public Goal(GoalIdentifier goalIdentifier, String name, int frequency, Date startingDate) {

        this.goalIdentifier = goalIdentifier;

        this.name = name;
        this.frequency = frequency;
        this.startingDate = startingDate;
    }

    public void addMuscleGroup(MuscleGroup muscleGroup) {
        muscleGroups.add(muscleGroup);
    }

    public static Goal of(GoalIdentifier goalIdentifier, String name, int frequency, Date creationDate) {
        return new Goal(goalIdentifier, name, frequency, creationDate);
    }

    public String getName() {
        return name;
    }

    public int getFrequency() {
        return frequency;
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

    public Date getStartingDate() {
        return startingDate;
    }

    public int getCreationWeek() {
        return DateUtil.getFromDate(startingDate, Calendar.WEEK_OF_YEAR);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setMuscleGroups(List<MuscleGroup> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }

    public GoalIdentifier getGoalIdentifier() {
        return goalIdentifier;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    @Override
    public Identifier getIdentifier() {
        return goalIdentifier;
    }
}