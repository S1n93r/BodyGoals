package com.slinger.bodygoals.ui.dtos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.GoalIdentifier;
import com.slinger.bodygoals.model.MuscleGroup;
import com.slinger.bodygoals.model.util.DateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.Getter;

public class GoalDto implements Comparable<GoalDto> {

    public static GoalDto EMPTY = new GoalDto(GoalIdentifier.of(-1), "", 0, LocalDate.now(), new ArrayList<>());

    @Getter
    private final GoalIdentifier goalIdentifier;

    @Getter
    private final String name;

    @Getter
    private final int frequency;
    
    private final List<MuscleGroup> muscleGroups;

    @Getter
    private final LocalDate creationDate;

    private GoalDto(@NonNull GoalIdentifier goalIdentifier, @NonNull String name, int frequency, @NonNull LocalDate creationDate,
                    @NonNull List<MuscleGroup> muscleGroups) {

        this.goalIdentifier = goalIdentifier;

        this.name = name;
        this.frequency = frequency;
        this.creationDate = creationDate;

        this.muscleGroups = muscleGroups;
    }

    public static GoalDto of(@NonNull GoalIdentifier goalIdentifier, @NonNull String name, int frequency, @NonNull LocalDate creationDate,
                             @NonNull List<MuscleGroup> muscleGroups) {
        return new GoalDto(goalIdentifier, name, frequency, creationDate, muscleGroups);
    }

    public static GoalDto from(Goal goal) {
        return GoalDto.of(goal.getGoalIdentifier(), goal.getName(), goal.getFrequency(), goal.getStartingDate(), goal.getMuscleGroupsCopy());
    }

    public Goal to() {

        Goal goal = new Goal(GoalIdentifier.of(goalIdentifier.getId()), name, frequency, creationDate);

        for (MuscleGroup muscleGroup : getMuscleGroupsCopy())
            goal.addMuscleGroup(muscleGroup);

        return goal;
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

    public List<MuscleGroup> getMuscleGroupsCopy() {
        return Collections.unmodifiableList(muscleGroups);
    }

    public int getCreationWeek() {
        return DateUtil.getWeekOfYear(creationDate);
    }
}