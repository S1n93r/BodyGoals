package com.slinger.bodygoals.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java8.util.Lists;

public class SessionLog {

    private static final String DAY_OF_WEEK = "EEEE";
    private static final String DAY = "dd";
    private static final String MONTH_TEXT = "MMM";
    private static final String MONTH = "MM";
    private static final String YEAR = "yyyy";

    private final Map<CalendarWeekIdentifier, List<Session>> loggedSessions = new HashMap<>();

    public void logSession(Goal goal) {

        Session session = new Session(goal);

        CalendarWeekIdentifier calendarWeekIdentifier = CalendarWeekIdentifier.from(session.getDate());

        loggedSessions.computeIfAbsent(calendarWeekIdentifier, k -> new ArrayList<>());

        loggedSessions.get(calendarWeekIdentifier).add(session);
    }

    public Set<CalendarWeekIdentifier> getLoggedWeeks() {
        return Collections.unmodifiableSet(loggedSessions.keySet());
    }

    public List<Session> getSessionsCopy(CalendarWeekIdentifier calendarWeekIdentifier) {

        List<Session> sessions = loggedSessions.get(calendarWeekIdentifier);

        if (sessions == null)
            return Lists.of();

        return Collections.unmodifiableList(sessions);
    }
}