package com.slinger.bodygoals.model;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import com.slinger.bodygoals.model.util.IdentifierUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class SessionLog {

    @ColumnInfo(name = "logged_sessions")
    @TypeConverters({SessionListConverter.class})
    private List<Session> loggedSessions = new ArrayList<>();

    public void logSession(Goal sessionGoal, Date sessionDate) {

        int id = IdentifierUtil.getNextId(loggedSessions);

        loggedSessions.add(new Session(SessionIdentifier.of(id), sessionGoal, sessionDate));
    }

    public Set<Integer> getLoggedWeeks() {

        return StreamSupport.stream(loggedSessions)
                .map(Session::getWeekOfYear)
                .collect(Collectors.toSet());
    }

    public List<Session> getSessionsWeekOfYear(Date date) {

        return StreamSupport.stream(loggedSessions)
                .filter(session -> DateUtil.compareDate(session.getDate(), date, Calendar.YEAR))
                .filter(session -> DateUtil.compareDate(session.getDate(), date, Calendar.WEEK_OF_YEAR))
                .collect(Collectors.toList());
    }

    public List<Session> getSessionsMonth(Date date) {

        return StreamSupport.stream(loggedSessions)
                .filter(session -> DateUtil.compareDate(session.getDate(), date, Calendar.YEAR))
                .filter(session -> DateUtil.compareDate(session.getDate(), date, Calendar.MONTH))
                .collect(Collectors.toList());
    }

    public Set<Goal> getSessionGoalsWeekOfYear(Date date) {

        return getSessionGoalsWeekOfYear(
                DateUtil.getFromDate(date, Calendar.YEAR),
                DateUtil.getFromDate(date, Calendar.WEEK_OF_YEAR));
    }

    private Set<Goal> getSessionGoalsWeekOfYear(int year, int weekOfYear) {

        return StreamSupport.stream(loggedSessions)
                .filter(session -> DateUtil.getFromDate(session.getDate(), Calendar.YEAR) == year)
                .filter(session -> DateUtil.getFromDate(session.getDate(), Calendar.WEEK_OF_YEAR) == weekOfYear)
                .map(Session::getGoal)
                .collect(Collectors.toSet());
    }

    public int getNumberOfSessionsLoggedWeekOfYear(Date date, GoalIdentifier goalIdentifier) {

        if (loggedSessions.isEmpty())
            return 0;


        List<Session> sessionsMatching = StreamSupport.stream(loggedSessions)
                .filter(session -> session.getGoal().getGoalIdentifier().equals(goalIdentifier))
                .filter(session -> DateUtil.compareDate(session.getDate(), date, Calendar.YEAR))
                .filter(session -> DateUtil.compareDate(session.getDate(), date, Calendar.WEEK_OF_YEAR))
                .collect(Collectors.toList());

        return sessionsMatching.size();
    }

    public void adjustGoalInSessions(GoalIdentifier goalIdentifier, String goalName, int frequency, List<MuscleGroup> muscleGroups) {

        getSessionsOfGoal(goalIdentifier).forEach(session -> {

            Goal goal = session.getGoal();

            goal.setName(goalName);
            goal.setFrequency(frequency);
            goal.setMuscleGroups(muscleGroups);
        });
    }

    public List<Session> getSessionsOfGoal(GoalIdentifier goalIdentifier) {

        return StreamSupport.stream(loggedSessions)
                .filter(session -> session.getGoal().getGoalIdentifier().equals(goalIdentifier))
                .collect(Collectors.toList());
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

        int maxProgress = 0;

        FirstWeekLastWeek firstWeekLastWeek = DateUtil.getFirstWeekAndLastWeekOfMonth(date);

        Set<Session> relevantSessions = new HashSet<>();

        for (int iWeek = firstWeekLastWeek.getFirst(); iWeek <= firstWeekLastWeek.getLast(); iWeek++) {

            for (Session loggedSession : loggedSessions) {
                if (DateUtil.getFromDate(loggedSession.getDate(), Calendar.WEEK_OF_YEAR) == iWeek)
                    relevantSessions.addAll(loggedSessions);
            }

            for (Goal goal : getSessionGoalsWeekOfYear(DateUtil.getFromDate(date, Calendar.YEAR), iWeek)) {
                maxProgress += goal.getFrequency();
            }
        }

        int progress = relevantSessions.size();

        return (int) Math.round((double) progress / (double) maxProgress * 100);
    }

    public int getGoalWeeklyProgress(Date date, Goal goal) {

        int sessionsLogged = getNumberOfSessionsLoggedWeekOfYear(date, goal.getGoalIdentifier());

        return (int) Math.round((double) sessionsLogged / (double) goal.getFrequency() * 100);
    }

    public List<Session> getLoggedSessions() {
        return loggedSessions;
    }

    public void setLoggedSessions(List<Session> loggedSessions) {
        this.loggedSessions = loggedSessions;
    }

    public void removeLogSession(SessionIdentifier sessionIdentifier) {
        loggedSessions.removeIf(session -> session.getSessionIdentifier().equals(sessionIdentifier));
    }

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