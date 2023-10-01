package com.slinger.bodygoals.ui;

import com.slinger.bodygoals.model.CalendarWeek;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.Session;
import com.slinger.bodygoals.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java8.util.Lists;
import java8.util.Maps;

public class ViewModel extends androidx.lifecycle.ViewModel {

    private final MutableLiveData<User> currentUser =
            new MutableLiveData<>(new User("Sl1ng3r"));

    private final MutableLiveData<CalendarWeek> selectedCalendarWeek =
            new MutableLiveData<>(CalendarWeek.from(Calendar.getInstance().getTime()));

    private final MutableLiveData<List<Goal>> userGoals = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Date> sessionDate = new MutableLiveData<>(Calendar.getInstance().getTime());

    public ViewModel() {
        registerLiveDataObserver();
    }

    private void registerLiveDataObserver() {
        currentUser.observeForever(user -> userGoals.setValue(user.getGoalsCopy()));
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<List<Goal>> getUserGoals() {
        return userGoals;
    }

    public LiveData<CalendarWeek> getSelectedCalendarWeek() {
        return selectedCalendarWeek;
    }

    public LiveData<Date> getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date date) {
        sessionDate.setValue(date);
    }

    public void selectPreviousWeek() {

        CalendarWeek calendarWeek = selectedCalendarWeek.getValue();

        if (calendarWeek != null)
            selectedCalendarWeek.setValue(CalendarWeek.previousWeek(calendarWeek));
    }

    public void selectNextWeek() {

        CalendarWeek calendarWeek = selectedCalendarWeek.getValue();

        if (calendarWeek != null)
            selectedCalendarWeek.setValue(CalendarWeek.nextWeek(calendarWeek));
    }

    public void addGoal(@NonNull Goal goal) {
        if (currentUser.getValue() != null)
            currentUser.getValue().addGoal(goal);
        else
            throw new IllegalStateException("User should not be 'null', as it was initialized with test user.");
    }

    public void addSessions(List<Session> sessions) {

        if (currentUser.getValue() != null) {

            for (Session session : sessions)
                currentUser.getValue().getSessionLog().logSession(session);
        } else {
            throw new IllegalStateException("User should not be 'null', as it was initialized with test user.");
        }
    }

    public List<Session> getSessions(CalendarWeek calendarWeek) {

        User user = currentUser.getValue();

        if (user == null)
            return Lists.of();

        return user.getSessionLog().getSessionsCopy(calendarWeek);
    }

    public Map<Goal, Integer> getCurrentProgress(CalendarWeek calendarWeek) {

        User user = currentUser.getValue();

        if (user == null)
            return Maps.of();

        return user.getSessionLog().getCurrentProgress(calendarWeek);
    }
}