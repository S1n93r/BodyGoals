package com.slinger.bodygoals.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.slinger.bodygoals.model.exceptions.GoalAlreadyExistsException;
import com.slinger.bodygoals.model.util.IdentifierUtil;

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

    public void addGoalWithNewId(String goalName, int frequency, Date startingDate, List<MuscleGroup> muscleGroups) throws GoalAlreadyExistsException {

        int id = IdentifierUtil.getNextId(goals);

        Goal goal = new Goal(GoalIdentifier.of(id), goalName, frequency, startingDate);

        muscleGroups.forEach(goal::addMuscleGroup);

        checkGoalNameAlreadyExists(goal);

        goals.add(goal);
    }

    public void addGoal(Goal goal) throws GoalAlreadyExistsException {

        checkGoalNameAlreadyExists(goal);

        goals.add(goal);
    }

    public void editGoal(GoalIdentifier goalIdentifier, String name, int frequency, List<MuscleGroup> muscleGroups) throws GoalAlreadyExistsException {

        boolean goalNotFound = true;

        for (Goal goalUser : goals) {

            if (goalUser.getGoalIdentifier().equals(goalIdentifier)) {

                checkGoalNameAlreadyExists(goalUser);

                goalUser.setName(name);
                goalUser.setFrequency(frequency);
                goalUser.setMuscleGroups(muscleGroups);

                goalNotFound = false;
            }
        }

        /* Adjust linked sessions. */
        sessionLog.adjustGoalInSessions(goalIdentifier, name, frequency, muscleGroups);

        if (goalNotFound)
            throw new IllegalStateException(String.format("Goal %s was not found.", name));
    }

    private void checkGoalNameAlreadyExists(Goal goal) throws GoalAlreadyExistsException {

        Set<String> goalNames = StreamSupport.stream(goals)
                .filter(goalFromUser -> !goalFromUser.getGoalIdentifier().equals(goal.getGoalIdentifier()))
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

    public void removeGoal(GoalIdentifier goalIdentifier) {
        goals.removeIf(goal -> goal.getGoalIdentifier().equals(goalIdentifier));
    }
}