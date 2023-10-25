package com.slinger.bodygoals.model;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class SessionLog {

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

    public List<Session> getSessionsCopy(int weekOfYear) {

        return StreamSupport.stream(loggedSessions)
                .filter(session -> session.getGoal().getCreationWeek() == weekOfYear)
                .collect(Collectors.toList());
    }

    public Set<Goal> getSessionGoals(int weekOfYear) {

        /* FIXME: Sorting is not working. */
        return StreamSupport.stream(getSessionsCopy(weekOfYear))
                .map(session -> session.getGoal())
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

    public int getOverallMonthlyProgress(int month) {

        /* TODO: Implement*/
        return 0;
    }

    public int getGoalProgress(int weekOfYear, Goal goal) {

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
    public Map<MuscleGroup, Progress> progressPerMuscleGroup(int weekOfYear) {

        List<Session> sessions = getSessionsCopy(weekOfYear);

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