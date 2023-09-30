package com.slinger.bodygoals.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java8.util.Lists;

public class SessionLog {

    private final Map<CalendarWeek, List<Session>> loggedSessions = new HashMap<>();

    public void logSession(Goal goal) {

        Session session = new Session(goal);

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
}