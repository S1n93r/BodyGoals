package com.slinger.bodygoals.model;

import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionLog {

    private static final String DAY_OF_WEEK = "EEEE";
    private static final String DAY = "dd";
    private static final String MONTH_TEXT = "MMM";
    private static final String MONTH = "MM";
    private static final String YEAR = "yyyy";

    private final Map<CalendarWeekIdentifier, List<Session>> loggedSessions = new HashMap<>();

    public void logSession(Goal goal) {

        Session session = new Session(goal);

        String dayString = DateFormat.format(DAY, session.getDate()).toString();
        String yearString = DateFormat.format(YEAR, session.getDate()).toString();

        int day = Integer.parseInt(dayString);
        int year = Integer.parseInt(yearString);

        CalendarWeekIdentifier calendarWeekIdentifier = CalendarWeekIdentifier.of(day, year);

        if (loggedSessions.get(calendarWeekIdentifier) == null)
            loggedSessions.put(CalendarWeekIdentifier.of(day, year), new ArrayList<>());

        loggedSessions.get(calendarWeekIdentifier).add(session);
    }
}