package com.slinger.bodygoals.model.log;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import com.slinger.bodygoals.model.util.DateUtil;
import com.slinger.bodygoals.model.FirstWeekLastWeek;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.GoalIdentifier;
import com.slinger.bodygoals.model.MuscleGroup;
import com.slinger.bodygoals.model.Progress;
import com.slinger.bodygoals.model.util.IdentifierUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class SessionLog {

    /* TODO: Switch to using years/weeks. */
    @Deprecated
    @ColumnInfo(name = "logged_sessions")
    @TypeConverters({SessionListConverter.class})
    private List<Session> loggedSessions = new ArrayList<>();

    @ColumnInfo(name = "logged_years")
    @TypeConverters({LoggedYearListConverter.class})
    private List<LoggedYear> loggedYears = new ArrayList<>();

    public void logSession(Goal sessionGoal, LocalDate sessionDate) {

        int id = IdentifierUtil.getNextId(loggedSessions);

        loggedSessions.add(new Session(SessionIdentifier.of(id), sessionGoal, sessionDate));
    }

    public Set<Integer> getLoggedWeeks() {

        return StreamSupport.stream(loggedSessions)
                .map(Session::getWeekOfYear)
                .collect(Collectors.toSet());
    }

    public List<Session> getSessionsWeekOfYear(LocalDate date) {

        return StreamSupport.stream(loggedSessions)
                .filter(session -> session.getDate().getYear() == date.getYear())
                .filter(session -> DateUtil.getWeekOfYear(session.getDate()) == DateUtil.getWeekOfYear(date))
                .collect(Collectors.toList());
    }

    public List<Session> getSessionsMonth(LocalDate date) {

        return StreamSupport.stream(loggedSessions)
                .filter(session -> session.getDate().getYear() == date.getYear())
                .filter(session -> session.getDate().getMonthValue() == date.getMonthValue())
                .collect(Collectors.toList());
    }

    public Set<Goal> getSessionGoalsWeekOfYear(LocalDate date) {

        return getSessionGoalsWeekOfYear(
                date.getYear(),
                DateUtil.getWeekOfYear(date));
    }

    private Set<Goal> getSessionGoalsWeekOfYear(int year, int weekOfYear) {

        return StreamSupport.stream(loggedSessions)
                .filter(session -> session.getDate().getYear() == year)
                .filter(session -> DateUtil.getWeekOfYear(session.getDate()) == weekOfYear)
                .map(Session::getGoal)
                .collect(Collectors.toSet());
    }

    public int getNumberOfSessionsLoggedWeekOfYear(LocalDate date, GoalIdentifier goalIdentifier) {

        if (loggedSessions.isEmpty())
            return 0;


        List<Session> sessionsMatching = StreamSupport.stream(loggedSessions)
                .filter(session -> session.getGoal().getGoalIdentifier().equals(goalIdentifier))
                .filter(session -> session.getDate().getYear() == date.getYear())
                .filter(session -> DateUtil.getWeekOfYear(session.getDate()) == DateUtil.getWeekOfYear(date))
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
                .filter(session -> session.getDate().getYear() == year)
                .forEach(session -> overallMonthlyProgressesMap.put(
                        session.getDate().getMonthValue(),
                        getMonthProgress(session.getDate())));

        return overallMonthlyProgressesMap;
    }

    private int getMonthProgress(LocalDate date) {

        int maxProgress = 0;

        FirstWeekLastWeek firstWeekLastWeek = DateUtil.getFirstWeekAndLastWeekOfMonth(date);

        Set<Session> relevantSessions = new HashSet<>();

        for (int iWeek = firstWeekLastWeek.getFirst(); iWeek <= firstWeekLastWeek.getLast(); iWeek++) {

            for (Session loggedSession : loggedSessions) {
                if (DateUtil.getWeekOfYear(loggedSession.getDate()) == iWeek)
                    relevantSessions.addAll(loggedSessions);
            }

            for (Goal goal : getSessionGoalsWeekOfYear(date.getYear(), iWeek)) {
                maxProgress += goal.getFrequency();
            }
        }

        int progress = relevantSessions.size();

        return (int) Math.round((double) progress / (double) maxProgress * 100);
    }

    public int getGoalWeeklyProgress(LocalDate date, Goal goal) {

        int sessionsLogged = getNumberOfSessionsLoggedWeekOfYear(date, goal.getGoalIdentifier());

        return (int) Math.round((double) sessionsLogged / (double) goal.getFrequency() * 100);
    }

    public List<Session> getLoggedSessions() {
        return loggedSessions;
    }

    public void setLoggedSessions(List<Session> loggedSessions) {
        this.loggedSessions = loggedSessions;
    }

    public List<LoggedYear> getLoggedYears() {
        return loggedYears;
    }

    public void setLoggedYears(List<LoggedYear> loggedYears) {
        this.loggedYears = loggedYears;
    }

    public void removeLogSession(SessionIdentifier sessionIdentifier) {
        loggedSessions.removeIf(session -> session.getSessionIdentifier().equals(sessionIdentifier));
    }

    public Map<MuscleGroup, Progress> progressPerMuscleGroup(LocalDate date) {

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