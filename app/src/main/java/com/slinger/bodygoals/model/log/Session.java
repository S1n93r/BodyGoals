package com.slinger.bodygoals.model.log;

import com.slinger.bodygoals.model.Goal;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Session {

    private final SessionIdentifier sessionIdentifier;
    private final Goal goal;

    private final LocalDate date;

    public Session(SessionIdentifier sessionIdentifier, Goal goal, LocalDate date) {
        this.sessionIdentifier = sessionIdentifier;
        this.goal = goal;
        this.date = date;
    }

    public Goal getGoal() {
        return goal;
    }

    public LocalDate getDate() {
        return date;
    }

    public SessionIdentifier getSessionIdentifier() {
        return sessionIdentifier;
    }

    public int getWeekOfYear() {
        return date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }
}