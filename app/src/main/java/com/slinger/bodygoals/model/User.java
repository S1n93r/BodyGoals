package com.slinger.bodygoals.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

/* TODO: User Immutable User */
@Entity
public class User {

    @PrimaryKey
    @NonNull
    private final String name;

    @ColumnInfo(name = "session_log")
    @TypeConverters({SessionLogConverter.class})
    private SessionLog sessionLog = new SessionLog();

    @ColumnInfo(name = "goals")
    @TypeConverters({GoalsConverter.class})
    private List<Goal> goals = new ArrayList<>();

    public User(@NonNull String name) {
        this.name = name;
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
    }

    public String getName() {
        return name;
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
}