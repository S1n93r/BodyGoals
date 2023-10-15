package com.slinger.bodygoals.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.slinger.bodygoals.model.exceptions.GoalAlreadyExistsException;

import java.util.ArrayList;
import java.util.Collections;
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

    public void addGoal(Goal goal) throws GoalAlreadyExistsException {

        Set<String> goalNames = StreamSupport.stream(goals)
                .map(Goal::getName)
                .collect(Collectors.toSet());

        if (goalNames.contains(goal.getName()))
            throw new GoalAlreadyExistsException();

        goals.add(goal);
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

    public List<Goal> getGoalsCopy() {
        return Collections.unmodifiableList(goals);
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