package com.slinger.bodygoals.model;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class SessionLog {

    /* TODO: To get data I constantly iterate over logs instead of saving progresses etc. in a DTO. */
    @ColumnInfo(name = "logged_sessions")
    @TypeConverters({SessionListConverter.class})
    private List<Session> loggedSessions = new ArrayList<>();

    public void logSession(Session session) {
        loggedSessions.add(session);
    }

    public Set<Integer> getLoggedWeeks() {

        return StreamSupport.stream(loggedSessions)
                .map(Session::getWeekOfYear)
                .collect(Collectors.toSet());
    }

    public List<Session> getSessionsWeekOfYear(Date date) {

        return StreamSupport.stream(loggedSessions)
                .filter(session -> DateUtil.compareDate(session.getGoal().getCreationDate(), date, Calendar.YEAR))
                .filter(session -> DateUtil.compareDate(session.getGoal().getCreationDate(), date, Calendar.WEEK_OF_YEAR))
                .collect(Collectors.toList());
    }

    public List<Session> getSessionsMonth(Date date) {

        return StreamSupport.stream(loggedSessions)
                .filter(session -> DateUtil.compareDate(session.getGoal().getCreationDate(), date, Calendar.YEAR))
                .filter(session -> DateUtil.compareDate(session.getGoal().getCreationDate(), date, Calendar.MONTH))
                .collect(Collectors.toList());
    }

    public Set<Goal> getSessionGoalsWeekOfYear(Date date) {

        /* FIXME: Sorting is not working. */
        return StreamSupport.stream(loggedSessions)
                .filter(session -> DateUtil.compareDate(session.getGoal().getCreationDate(), date, Calendar.YEAR))
                .filter(session -> DateUtil.compareDate(session.getGoal().getCreationDate(), date, Calendar.WEEK_OF_YEAR))
                .map(Session::getGoal)
                .sorted()
                .collect(Collectors.toSet());
    }

    public int getSessionsLogged(int weekOfYear, Goal goal) {

        if (loggedSessions.isEmpty())
            return 0;

        List<Session> sessionsMatching = StreamSupport.stream(loggedSessions)
                .filter(session -> session.getGoal().equals(goal))
                .filter(session -> session.getGoal().getCreationWeek() == weekOfYear)
                .collect(Collectors.toList());

        return sessionsMatching.size();
    }

    public Map<Integer, Integer> getOverallMonthlyProgresses(int year) {

        Map<Integer, Integer> overallMonthlyProgressesMap = new HashMap<>();

        for (int iMonth = Calendar.JANUARY; iMonth <= Calendar.DECEMBER; iMonth++)
            overallMonthlyProgressesMap.put(iMonth, 0);

        StreamSupport.stream(loggedSessions)
                .filter(session -> DateUtil.getFromDate(session.getDate(), Calendar.YEAR) == year)
                .forEach(session -> overallMonthlyProgressesMap.put(
                        DateUtil.getFromDate(session.getDate(), Calendar.MONTH),
                        getMonthProgress(session.getDate())));

        return overallMonthlyProgressesMap;
    }

    private int getMonthProgress(Date date) {

        List<Session> sessionsInMonth = getSessionsMonth(date);

        int maxProgress = 0;
        int progress = sessionsInMonth.size();

        Set<Goal> goals = StreamSupport.stream(sessionsInMonth)
                .map(Session::getGoal)
                .collect(Collectors.toSet());

        /* FIXME: This is per week, not per month. */
        for (Goal goal : goals)
            maxProgress += goal.getFrequency();

        return (int) Math.round((double) progress / (double) maxProgress * 100);
    }

    public int getGoalWeeklyProgress(int weekOfYear, Goal goal) {

        int sessionsLogged = getSessionsLogged(weekOfYear, goal);

        return (int) Math.round((double) sessionsLogged / (double) goal.getFrequency() * 100);
    }

    public List<Session> getLoggedSessions() {
        return loggedSessions;
    }

    public void setLoggedSessions(List<Session> loggedSessions) {
        this.loggedSessions = loggedSessions;
    }

    public void removeLogSession(Session session) {
        loggedSessions.remove(session);
    }

    /* TODO: Dedicated class instead of map, otherwise every place has to null-check. */
    /* FIXME: Muscle group progress is applied to wrong groups. Check here and in goals. */
    public Map<MuscleGroup, Progress> progressPerMuscleGroup(Date date) {

        List<Session> sessions = getSessionsWeekOfYear(date);

        Map<MuscleGroup, Progress> progressPerMuscleGroup = new HashMap<>();

        int maxFrequency = 0;

        for (Session session : sessions) {

            int frequency = session.getGoal().getFrequency();

            if (frequency > maxFrequency)
                maxFrequency = frequency;
        }

        for (MuscleGroup value : MuscleGroup.values())
            progressPerMuscleGroup.put(value, Progress.of(maxFrequency, 0));

        sessions.forEach(session ->
                session.getGoal().getMuscleGroupsCopy().forEach(muscleGroup -> {

                    Progress progress = progressPerMuscleGroup.get(muscleGroup);

                    if (progress == null)
                        throw new IllegalStateException("Progress should not be null here. Check.");

                    progress.increaseCurrent();
                }));

        return progressPerMuscleGroup;
    }
}