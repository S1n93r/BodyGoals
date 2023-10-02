package com.slinger.bodygoals.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import java8.util.Lists;

@Entity
public class SessionLog {

    @PrimaryKey
    private int logId = 12345;

    @Ignore
    private Map<CalendarWeek, List<Session>> loggedSessions;

    @ColumnInfo
    @TypeConverters({SessionListConverter.class})
    private List<Session> sessions = new ArrayList<>();

    public void loadMap(List<Session> sessions) {

        Map<CalendarWeek, List<Session>> loggedSessions = new HashMap<>();

        for (Session session : sessions) {

            CalendarWeek calendarWeek = CalendarWeek.from(session.getDate());

            loggedSessions.computeIfAbsent(calendarWeek, cw -> new ArrayList<>());

            loggedSessions.get(calendarWeek).add(session);
        }
    }

    public void logSession(Session session) {

        CalendarWeek calendarWeek = CalendarWeek.from(session.getDate());

        loggedSessions.computeIfAbsent(calendarWeek, k -> new ArrayList<>());

        loggedSessions.get(calendarWeek).add(session);
    }

    public Set<CalendarWeek> getLoggedWeeks() {
        return Collections.unmodifiableSet(loggedSessions.keySet());
    }

    public List<Session> getSessionsCopy(CalendarWeek calendarWeek) {

        List<Session> sessions = loggedSessions.get(calendarWeek);

        if (sessions == null)
            return Lists.of();

        return Collections.unmodifiableList(sessions);
    }

    public int getSessionsLogged(CalendarWeek calendarWeek, Goal goal) {

        List<Session> sessions = loggedSessions.get(calendarWeek);

        if (sessions == null)
            return 0;

        List<Session> sessionsMatching = new ArrayList<>();

        for (Session session : sessions)
            if (session.getGoal() == goal)
                sessionsMatching.add(session);

        return sessionsMatching.size();
    }

    public int getGoalProgress(CalendarWeek calendarWeek, Goal goal) {

        int sessionsLogged = getSessionsLogged(calendarWeek, goal);

        return (int) Math.round((double) sessionsLogged / (double) goal.getFrequency() * 100);
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
        loadMap(sessions);
    }
}