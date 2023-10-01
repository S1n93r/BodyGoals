package com.slinger.bodygoals.model;

import java.util.Date;

public class Session {

    private final Goal goal;
    private final Date date;

    public Session(Goal goal, Date date) {
        this.goal = goal;
        this.date = date;
    }

    public Goal getGoal() {
        return goal;
    }

    public Date getDate() {
        return date;
    }
}