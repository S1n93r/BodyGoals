package com.slinger.bodygoals.ui;

import com.slinger.bodygoals.model.CalendarWeek;
import com.slinger.bodygoals.model.User;

import java.util.Calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OverviewViewModel extends ViewModel {

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
}