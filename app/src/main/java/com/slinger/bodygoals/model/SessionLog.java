package com.slinger.bodygoals.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java8.util.Lists;
import java8.util.Maps;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class SessionLog {

    private final Map<CalendarWeek, List<Session>> loggedSessions = new HashMap<>();

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

    public Map<Goal, Integer> getCurrentProgress(CalendarWeek calendarWeek) {

        List<Session> sessions = loggedSessions.get(calendarWeek);

        if (sessions == null)
            return Maps.of();

        Set<Goal> goals = StreamSupport.stream(sessions)
                .map(Session::getGoal)
                .collect(Collectors.toSet());

        Map<Goal, Integer> goalNameToProgressMap = new HashMap<>(goals.size());

        for (Goal goal : goals) {

            goalNameToProgressMap.putIfAbsent(goal, 0);

            for (Session session : sessions)
                if (goal.getName().equals(session.getGoal().getName()))
                    goalNameToProgressMap.put(goal, goalNameToProgressMap.get(goal) + 1);

        }

        return goalNameToProgressMap;
    }
}