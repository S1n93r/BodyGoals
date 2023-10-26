package com.slinger.bodygoals.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.slinger.bodygoals.model.exceptions.GoalAlreadyExistsException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

@Entity
public class User {

    @PrimaryKey
    private int userId = 123;

    @Embedded
    private SessionLog sessionLog = new SessionLog();

    @ColumnInfo(name = "goals")
    @TypeConverters({GoalListConverter.class})
    private List<Goal> goals = new ArrayList<>();

    public void addGoal(String goalName, int frequency, Date startingDate, List<MuscleGroup> muscleGroups) throws GoalAlreadyExistsException {

        int maxId = 0;

        for (int id : StreamSupport.stream(goals).map(goal -> goal.getGoalIdentifier().getId()).collect(Collectors.toSet()))
            if (id > maxId)
                maxId = id;

        Goal goal = new Goal(GoalIdentifier.of(maxId + 1), goalName, frequency, startingDate);

        muscleGroups.forEach(goal::addMuscleGroup);

        checkGoalNameAlreadyExists(goal);

        goals.add(goal);
    }

    public void editGoal(Goal goal) throws GoalAlreadyExistsException {

        checkGoalNameAlreadyExists(goal);

        boolean goalNotFound = true;

        for (Goal goalUser : goals) {

            if (goalUser.equals(goal)) {

                int originalIndex = goals.indexOf(goalUser);

                goals.remove(originalIndex);
                goals.add(originalIndex, goalUser);

                goalNotFound = false;
            }
        }

        if (goalNotFound)
            throw new IllegalStateException(String.format("Goal %s was not found.", goal.getName()));
    }

    private void checkGoalNameAlreadyExists(Goal goal) throws GoalAlreadyExistsException {

        Set<String> goalNames = StreamSupport.stream(goals)
                .map(Goal::getName)
                .collect(Collectors.toSet());

        if (goalNames.contains(goal.getName()))
            throw new GoalAlreadyExistsException();
    }

    public SessionLog getSessionLog() {
        return sessionLog;
    }

    public void setSessionLog(SessionLog sessionLog) {
        this.sessionLog = sessionLog;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void removeGoal(Goal goal) {
        goals.remove(goal);
    }
}