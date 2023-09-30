package com.slinger.bodygoals.model;

import java.util.Calendar;
import java.util.Date;

public class Session {

    private final Goal goal;
    private final Date date;

    public Session(Goal goal) {
        this.goal = goal;
        this.date = Calendar.getInstance().getTime();
    }

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