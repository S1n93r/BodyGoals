package com.slinger.bodygoals.ui;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.slinger.bodygoals.model.BodyGoalDatabase;
import com.slinger.bodygoals.model.CalendarWeek;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.Session;
import com.slinger.bodygoals.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import java8.util.Lists;

public class ViewModel extends AndroidViewModel {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    private final MutableLiveData<User> currentUser =
            new MutableLiveData<>(new User());

    private final MutableLiveData<CalendarWeek> selectedCalendarWeek =
            new MutableLiveData<>(CalendarWeek.from(Calendar.getInstance().getTime()));

    private final MutableLiveData<List<Goal>> userGoals = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Date> sessionDate = new MutableLiveData<>(Calendar.getInstance().getTime());

    private BodyGoalDatabase database;

    public ViewModel(@NonNull Application application) {

        super(application);

        registerLiveDataObserver();

        database = Room.databaseBuilder(application, BodyGoalDatabase.class, "body-goals-database").build();

        executor.execute(() -> {

            User user = database.userDao().findByName(123);

            if (user != null)
                handler.post(() -> currentUser.setValue(user));
        });
    }

    private void registerLiveDataObserver() {
        currentUser.observeForever(user -> {
            /* TODO: Null-semantic is not nice */
            if (user != null)
                userGoals.setValue(user.getGoalsCopy());
        });
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

        /* TODO: User Immutable User */
        updateUser();
    }

    public void addSessions(List<Session> sessions) {

        if (currentUser.getValue() == null)
            return;

        if (currentUser.getValue().getSessionLog() == null)
            return;

        for (Session session : sessions)
            currentUser.getValue().getSessionLog().logSession(session);

        /* TODO: User Immutable User */
        updateUser();
    }

    public List<Session> getSessions(CalendarWeek calendarWeek) {

        User user = currentUser.getValue();

        if (user == null)
            return Lists.of();

        return user.getSessionLog().getSessionsCopy(calendarWeek);
    }

    public int getGoalProgress(CalendarWeek calendarWeek, Goal goal) {

        User user = currentUser.getValue();

        if (user == null)
            return 0;

        if (user.getSessionLog() == null)
            return 0;

        return user.getSessionLog().getGoalProgress(calendarWeek, goal);
    }

    public int getSessionsLogged(CalendarWeek calendarWeek, Goal goal) {

        User user = currentUser.getValue();

        if (user == null)
            return 0;

        if (user.getSessionLog() == null)
            return 0;

        return user.getSessionLog().getSessionsLogged(calendarWeek, goal);
    }

    private void updateUser() {

        User user = currentUser.getValue();

        currentUser.setValue(null);
        currentUser.setValue(user);

        if (database != null)
            executor.execute(() -> database.userDao().insertAll(user));
    }
}