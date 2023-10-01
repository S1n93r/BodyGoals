package com.slinger.bodygoals.ui;

import com.slinger.bodygoals.model.CalendarWeek;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.Session;
import com.slinger.bodygoals.model.User;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModel extends androidx.lifecycle.ViewModel {

    private final MutableLiveData<User> currentUser =
            new MutableLiveData<>(new User("Sl1ng3r"));

    private final MutableLiveData<CalendarWeek> selectedCalendarWeek =
            new MutableLiveData<>(CalendarWeek.from(Calendar.getInstance().getTime()));

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<CalendarWeek> getSelectedCalendarWeek() {
        return selectedCalendarWeek;
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

            /* TODO: Remove test-sout */
            System.out.println(currentUser.getValue().getGoalsCopy());
        } else {
            throw new IllegalStateException("User should not be 'null', as it was initialized with test user.");
        }
    }
}