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

public class SessionLog {

    @Ignore
    private Map<CalendarWeek, List<Session>> calenderWeekToSessionMap = new HashMap<>();

    @ColumnInfo(name = "logged_sessions")
    @TypeConverters({SessionListConverter.class})
    private List<Session> loggedSessions = new ArrayList<>();

    public void loadMapIfAbsent(List<Session> sessions) {

        if (calenderWeekToSessionMap != null)
            return;

        calenderWeekToSessionMap = new HashMap<>();

        for (Session session : sessions) {

            CalendarWeek calendarWeek = CalendarWeek.from(session.getDate());

            calenderWeekToSessionMap.computeIfAbsent(calendarWeek, cw -> new ArrayList<>());

            calenderWeekToSessionMap.get(calendarWeek).add(session);
        }
    }

    public void logSession(Session session) {

        if (calenderWeekToSessionMap == null)
            calenderWeekToSessionMap = new HashMap<>();

        CalendarWeek calendarWeek = CalendarWeek.from(session.getDate());

        calenderWeekToSessionMap.computeIfAbsent(calendarWeek, k -> new ArrayList<>());

        calenderWeekToSessionMap.get(calendarWeek).add(session);

        transferToSessionList(calenderWeekToSessionMap);
    }

    public Set<CalendarWeek> getLoggedWeeks() {
        return Collections.unmodifiableSet(calenderWeekToSessionMap.keySet());
    }

    public List<Session> getSessionsCopy(CalendarWeek calendarWeek) {

        List<Session> sessions = calenderWeekToSessionMap.get(calendarWeek);

        if (sessions == null)
            return Lists.of();

        return Collections.unmodifiableList(sessions);
    }

    public int getSessionsLogged(CalendarWeek calendarWeek, Goal goal) {

        if (calenderWeekToSessionMap == null)
            return 0;

        List<Session> sessions = calenderWeekToSessionMap.get(calendarWeek);

        if (sessions == null)
            return 0;

        List<Session> sessionsMatching = new ArrayList<>();

        for (Session session : sessions)
            if (session.getGoal().getName().equals(goal.getName()))
                sessionsMatching.add(session);

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
        loadMapIfAbsent(loggedSessions);
    }

    private void transferToSessionList(Map<CalendarWeek, List<Session>> calenderWeekToSessionMap) {

        List<Session> allSessions = new ArrayList<>();

        for (List<Session> sessions : calenderWeekToSessionMap.values())
            allSessions.addAll(sessions);

        loggedSessions = allSessions;
    }

    public void removeSessionsBelongingToGoal(Goal goal) {
        
        loggedSessions.removeIf(session -> session.getGoal().getName().equals(goal.getName()));

        for (List<Session> sessions : calenderWeekToSessionMap.values())
            sessions.removeIf(session -> session.getGoal().getName().equals(goal.getName()));
    }
}