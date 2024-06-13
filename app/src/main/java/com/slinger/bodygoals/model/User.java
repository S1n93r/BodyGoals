package com.slinger.bodygoals.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.slinger.bodygoals.model.exceptions.GoalAlreadyExistsException;
import com.slinger.bodygoals.model.exercises.Exercise;
import com.slinger.bodygoals.model.exercises.ExerciseIdentifier;
import com.slinger.bodygoals.model.exercises.ExerciseListConverter;
import com.slinger.bodygoals.model.log.SessionLog;
import com.slinger.bodygoals.model.util.IdentifierUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class User {

    @PrimaryKey
    private int userId = 123;

    @Embedded
    private SessionLog sessionLog = new SessionLog();

    @ColumnInfo(name = "goals")
    @TypeConverters({GoalListConverter.class})
    private List<Goal> goals = new ArrayList<>();

    @ColumnInfo(name = "exercises")
    @TypeConverters({ExerciseListConverter.class})
    private List<Exercise> exercises = new ArrayList<>();

    public void addGoalWithNewId(String goalName, int frequency, LocalDate startingDate, List<MuscleGroup> muscleGroups) throws GoalAlreadyExistsException {

        int id = IdentifierUtil.getNextGoalId(goals);

        Goal goal = new Goal(GoalIdentifier.of(id), goalName, frequency, startingDate);

        muscleGroups.forEach(goal::addMuscleGroup);

        checkGoalNameAlreadyExists(goal);

        goals.add(goal);
    }

    public void editGoal(GoalIdentifier goalIdentifier, String name, int frequency, LocalDate startingDate, List<MuscleGroup> muscleGroups) throws GoalAlreadyExistsException {

        List<Goal> matchingGoals = StreamSupport.stream(goals)
                .filter(goalUser -> goalUser.getGoalIdentifier().equals(goalIdentifier))
                .collect(Collectors.toList());

        if (matchingGoals.isEmpty())
            throw new IllegalStateException(String.format("Goal %s was not found.", name));

        if (matchingGoals.size() > 1)
            throw new IllegalStateException(String.format("Identifier %s was found multiple times. Check.", goalIdentifier));

        Goal goalEdit = matchingGoals.get(0);

        checkGoalNameAlreadyExists(goalEdit);

        goalEdit.setName(name);
        goalEdit.setFrequency(frequency);
        goalEdit.setStartingDate(startingDate);
        goalEdit.setMuscleGroups(muscleGroups);

        /* Adjust linked sessions. */
        sessionLog.adjustGoalInSessions(goalIdentifier, name, frequency, muscleGroups);
    }

    private void checkGoalNameAlreadyExists(Goal goal) throws GoalAlreadyExistsException {

        Set<String> goalNames = StreamSupport.stream(goals)
                .filter(goalFromUser -> !goalFromUser.getGoalIdentifier().equals(goal.getGoalIdentifier()))
                .map(Goal::getName)
                .collect(Collectors.toSet());

        if (goalNames.contains(goal.getName()))
            throw new GoalAlreadyExistsException();
    }

    public void removeGoal(GoalIdentifier goalIdentifier) {
        goals.removeIf(goal -> goal.getGoalIdentifier().equals(goalIdentifier));
    }

    public void addExercise(Exercise exercise) {

        long matchingIdentifier = StreamSupport.stream(exercises)
                .map(Exercise::getExerciseIdentifier)
                .filter(exerciseIdentifier -> exercise.getExerciseIdentifier().equals(exerciseIdentifier))
                .count();

        if (matchingIdentifier > 0)
            throw new IllegalStateException(String.format("An exercise with the identifier %s was already added.",
                    exercise.getExerciseIdentifier()));

        exercises.add(exercise);
    }

    public void removeExercise(ExerciseIdentifier exerciseIdentifier) {
        exercises.removeIf(exercise -> exercise.getExerciseIdentifier().equals(exerciseIdentifier));
    }
}