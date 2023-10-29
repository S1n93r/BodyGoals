package com.slinger.bodygoals.model;

import com.slinger.bodygoals.ui.dtos.Identifieable;

import java.util.Calendar;
import java.util.Date;

public class Session implements Identifieable {

    private final SessionIdentifier sessionIdentifier;
    private final Goal goal;
    private final Date date;

    public Session(SessionIdentifier sessionIdentifier, Goal goal, Date date) {
        this.sessionIdentifier = sessionIdentifier;
        this.goal = goal;
        this.date = date;
    }

    public Goal getGoal() {
        return goal;
    }

    public Date getDate() {
        return date;
    }

    public SessionIdentifier getSessionIdentifier() {
        return sessionIdentifier;
    }

    public int getWeekOfYear() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    @Override
    public Identifier getIdentifier() {
        return sessionIdentifier;
    }
}