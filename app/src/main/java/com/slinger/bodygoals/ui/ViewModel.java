package com.slinger.bodygoals.ui;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.slinger.bodygoals.model.BodyGoalDatabase;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.Session;
import com.slinger.bodygoals.model.User;
import com.slinger.bodygoals.model.exceptions.GoalAlreadyExistsException;
import com.slinger.bodygoals.ui.dtos.YearlySummaryDto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java8.util.Lists;
import java8.util.Sets;

public class ViewModel extends AndroidViewModel {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    private final MutableLiveData<User> currentUser =
            new MutableLiveData<>(new User());

    private final MutableLiveData<Integer> selectedCalendarWeek =
            new MutableLiveData<>(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));

    private final MutableLiveData<Integer> selectedYear =
            new MutableLiveData<>(Calendar.getInstance().get(Calendar.YEAR));

    private final MutableLiveData<List<Goal>> userGoals = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Date> sessionDate = new MutableLiveData<>(Calendar.getInstance().getTime());

    private final MutableLiveData<YearlySummaryDto> yearlySummaryDtoMutableLiveData =
            new MutableLiveData<>(YearlySummaryDto.EMPTY);

    private final BodyGoalDatabase database;

    private List<Session> preSavedSessions;

    public ViewModel(@NonNull Application application) {

        super(application);

        registerLiveDataObserver();

        database = Room.databaseBuilder(application, BodyGoalDatabase.class, "body-goals-database").build();
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

    public LiveData<Integer> getSelectedCalendarWeek() {
        return selectedCalendarWeek;
    }

    public LiveData<Date> getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date date) {
        sessionDate.setValue(date);
    }

    public void selectPreviousWeek() {

        Integer weekOfYear = selectedCalendarWeek.getValue();

        /* TODO: Handle logic in DTO. */
        if (weekOfYear == null || weekOfYear == 1)
            return;

        selectedCalendarWeek.setValue(weekOfYear - 1);
    }

    public void selectNextWeek() {

        Integer weekOfYear = selectedCalendarWeek.getValue();

        /* TODO: Handle logic in DTO. */
        if (weekOfYear == null || weekOfYear == 52)
            return;

        selectedCalendarWeek.setValue(weekOfYear + 1);
    }

    public void addGoal(@NonNull Goal goal) throws GoalAlreadyExistsException {

        if (currentUser.getValue() != null)
            currentUser.getValue().addGoal(goal);
        else
            throw new IllegalStateException("User should not be 'null', as it was initialized with test user.");

        updateUser();
    }

    public void addSessions(List<Session> sessions) {

        if (currentUser.getValue() == null)
            return;

        for (Session session : sessions)
            currentUser.getValue().getSessionLog().logSession(session);

        updateUser();
    }

    public List<Session> getSessions(int weekOfYear) {

        User user = currentUser.getValue();

        if (user == null)
            return Lists.of();

        return user.getSessionLog().getSessionsCopy(weekOfYear);
    }

    public Set<Goal> getSessionGoals(int weekOfYear) {

        User user = currentUser.getValue();

        if (user == null)
            return Sets.of();

        return user.getSessionLog().getSessionGoals(weekOfYear);
    }

    public int getGoalProgress(int yearOfWeek, Goal goal) {

        User user = currentUser.getValue();

        if (user == null)
            return 0;

        return user.getSessionLog().getGoalProgress(yearOfWeek, goal);
    }

    public int getSessionsLogged(int weekOfYear, Goal goal) {

        User user = currentUser.getValue();

        if (user == null)
            return 0;

        if (user.getSessionLog() == null)
            return 0;

        return user.getSessionLog().getSessionsLogged(weekOfYear, goal);
    }

    private void updateUser() {

        User user = currentUser.getValue();

        /* TODO: There should be a immutable UserDto for ViewClasses to use. */
        currentUser.setValue(null);
        currentUser.setValue(user);

        if (database != null)
            executor.execute(() -> database.userDao().insertAll(currentUser.getValue()));
    }

    public void loadUser() {

        executor.execute(() -> {

            User user = database.userDao().findByName(123);

            if (user != null)
                handler.post(() -> currentUser.setValue(user));
        });
    }

    public void deleteGoal(Goal goal) {

        User user = currentUser.getValue();

        if (user == null)
            return;

        user.removeGoal(goal);

        updateUser();
    }

    public void removeLogEntry(Session session) {

        if (currentUser.getValue() == null)
            return;

        currentUser.getValue().getSessionLog().removeLogSession(session);

        updateUser();
    }

    public List<Session> getPreSavedSessions() {
        return preSavedSessions;
    }

    public void setPreSavedSessions(List<Session> preSavedSessions) {
        this.preSavedSessions = preSavedSessions;
    }

    public MutableLiveData<Integer> getSelectedYear() {
        return selectedYear;
    }

    public void selectedPreviousYear() {

        if (selectedYear.getValue() == null)
            return;

        int selectedYearInt = selectedYear.getValue();

        if (selectedYearInt - 1 > 1990)
            selectedYearInt--;

        selectedYear.setValue(selectedYearInt);
    }

    public void selectedNextYear() {

        if (selectedYear.getValue() == null)
            return;

        int selectedYearInt = selectedYear.getValue();

        if (selectedYearInt + 1 < 9999)
            selectedYearInt++;

        selectedYear.setValue(selectedYearInt);
    }
}