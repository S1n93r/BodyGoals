package com.slinger.bodygoals.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Goal implements Comparable<Goal> {

    public static Goal EMPTY = new Goal("", 0, Calendar.getInstance().getTime());

    private final String name;
    private final int frequency;
    private final List<MuscleGroup> muscleGroups = new ArrayList<>();

    private final Date creationDate;

    public Goal(String name, int frequency, Date creationDate) {
        this.name = name;
        this.frequency = frequency;
        this.creationDate = creationDate;
    }

    public void addMuscleGroup(MuscleGroup muscleGroup) {
        muscleGroups.add(muscleGroup);
    }

    public static Goal of(String name, int frequency, Date creationDate) {
        return new Goal(name, frequency, creationDate);
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

    public Date getCreationDate() {
        return creationDate;
    }

    public int getCreationWeek() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationDate);

        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
}