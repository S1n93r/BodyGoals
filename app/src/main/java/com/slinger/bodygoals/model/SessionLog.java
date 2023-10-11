package com.slinger.bodygoals.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.TypeConverters;
import java8.util.Lists;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class SessionLog {

    @ColumnInfo(name = "logged_sessions")
    @TypeConverters({SessionListConverter.class})
    private List<Session> loggedSessions = new ArrayList<>();

    public void logSession(Session session) {
        loggedSessions.add(session);
    }

    public Set<CalendarWeek> getLoggedWeeks() {

        return StreamSupport.stream(loggedSessions)
                .map(session -> CalendarWeek.from(session.getDate()))
                .collect(Collectors.toSet());
    }

    public List<Session> getSessionsCopy(CalendarWeek calendarWeek) {

        return StreamSupport.stream(loggedSessions)
                .filter(session -> CalendarWeek.from(session.getDate()).equals(calendarWeek))
                .collect(Collectors.toList());
    }

    public int getSessionsLogged(CalendarWeek calendarWeek, Goal goal) {

        if (loggedSessions.isEmpty())
            return 0;

        List<Session> sessionsMatching = StreamSupport.stream(loggedSessions)
                .filter(session -> session.getGoal().equals(goal))
                .filter(session -> CalendarWeek.from(session.getDate()).equals(calendarWeek))
                .collect(Collectors.toList());

        return sessionsMatching.size();
    }

    public int getGoalProgress(CalendarWeek calendarWeek, Goal goal) {

        int sessionsLogged = getSessionsLogged(calendarWeek, goal);

        return (int) Math.round((double) sessionsLogged / (double) goal.getFrequency() * 100);
    }

    public List<Session> getLoggedSessions() {
        return loggedSessions;
    }

    public void setLoggedSessions(List<Session> loggedSessions) {
        this.loggedSessions = loggedSessions;
    }

    public void removeSessionsBelongingToGoal(Goal goal) {
        loggedSessions.removeIf(session -> session.getGoal().getName().equals(goal.getName()));
    }
}