package com.slinger.bodygoals.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private final String name;
    private final SessionLog sessionLog = new SessionLog();

    private final List<Goal> goals = new ArrayList<>();

    public User(String name) {
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

    public List<Goal> getGoalsCopy() {
        return Collections.unmodifiableList(goals);
    }
}