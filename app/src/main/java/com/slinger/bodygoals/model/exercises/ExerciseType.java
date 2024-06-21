package com.slinger.bodygoals.model.exercises;

import com.slinger.bodygoals.model.MuscleGroup;

import java.util.List;

import java8.util.Lists;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

public enum ExerciseType {

    /* Shoulders */
    LATERAL_RAISES(Lists.of(MuscleGroup.SHOULDERS, MuscleGroup.NECK)),
    MILITARY_PRESS(Lists.of(MuscleGroup.SHOULDERS, MuscleGroup.NECK)),

    /* Chest */
    CHEST_PRESS(Lists.of(MuscleGroup.CHEST, MuscleGroup.TRICEPS)),
    PUSH_UPS(Lists.of(MuscleGroup.CHEST, MuscleGroup.TRICEPS)),

    /* Back */
    PULL_UPS(Lists.of(MuscleGroup.LATS, MuscleGroup.BICEPS, MuscleGroup.FOREARMS)),
    PULL_DOWNS(Lists.of(MuscleGroup.LATS, MuscleGroup.BICEPS, MuscleGroup.FOREARMS)),

    /* Legs */
    LEG_PRESS(Lists.of(MuscleGroup.QUADS)),
    LEG_CURLS(Lists.of(MuscleGroup.HARM_STRINGS));

    private final List<MuscleGroup> muscleGroups;

    ExerciseType(List<MuscleGroup> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }

    public Stream<MuscleGroup> getMuscleGroupsStream() {
        return StreamSupport.stream(muscleGroups);
    }

    public String getName() {
        return name().replaceAll("_", " ").toLowerCase();
    }
}