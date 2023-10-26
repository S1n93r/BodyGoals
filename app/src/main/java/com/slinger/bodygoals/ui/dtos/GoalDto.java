package com.slinger.bodygoals.ui.dtos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.slinger.bodygoals.model.DateUtil;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.GoalIdentifier;
import com.slinger.bodygoals.model.MuscleGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GoalDto implements Comparable<GoalDto> {

    public static GoalDto EMPTY = new GoalDto(GoalIdentifier.of(-1), "", 0, Calendar.getInstance().getTime(), new ArrayList<>());

    private final GoalIdentifier goalIdentifier;

    private final String name;
    private final int frequency;
    private final List<MuscleGroup> muscleGroups;

    private final Date creationDate;

    public GoalDto(@NonNull GoalIdentifier goalIdentifier, @NonNull String name, int frequency, @NonNull Date creationDate,
                   @NonNull List<MuscleGroup> muscleGroups) {

        this.goalIdentifier = goalIdentifier;

        this.name = name;
        this.frequency = frequency;
        this.creationDate = creationDate;

        this.muscleGroups = muscleGroups;
    }

    public static GoalDto of(@NonNull GoalIdentifier goalIdentifier, @NonNull String name, int frequency, @NonNull Date creationDate,
                             @NonNull List<MuscleGroup> muscleGroups) {
        return new GoalDto(goalIdentifier, name, frequency, creationDate, muscleGroups);
    }

    /* TODO: Convert to Util class? */
    public static GoalDto from(Goal goal) {
        return GoalDto.of(goal.getGoalIdentifier(), goal.getName(), goal.getFrequency(), goal.getCreationDate(), goal.getMuscleGroupsCopy());
    }

    /* TODO: Convert to Util class? */
    public Goal to() {

        Goal goal = new Goal(GoalIdentifier.of(goalIdentifier.getId()), name, frequency, new Date(creationDate.getTime()));

        for (MuscleGroup muscleGroup : getMuscleGroupsCopy())
            goal.addMuscleGroup(muscleGroup);

        return goal;
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

        return goalIdentifier.equals(((GoalDto) o).getGoalIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(GoalDto o) {
        return name.toLowerCase().compareTo(o.name.toLowerCase());
    }

    public GoalIdentifier getGoalIdentifier() {
        return goalIdentifier;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public List<MuscleGroup> getMuscleGroupsCopy() {
        return Collections.unmodifiableList(muscleGroups);
    }

    public int getCreationWeek() {
        return DateUtil.getFromDate(creationDate, Calendar.WEEK_OF_YEAR);
    }
}