package com.slinger.bodygoals.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class User {

    @PrimaryKey
    private int userId = 123;

    @Embedded
    private SessionLog sessionLog = new SessionLog();

    @ColumnInfo(name = "goals")
    @TypeConverters({GoalListConverter.class})
    private List<Goal> goals = new ArrayList<>();

    /* TODO: Make sure you can't add a goal with the same name. */
    public void addGoal(Goal goal) {
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