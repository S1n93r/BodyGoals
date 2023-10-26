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
import com.slinger.bodygoals.model.DateUtil;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.Session;
import com.slinger.bodygoals.model.User;
import com.slinger.bodygoals.model.exceptions.GoalAlreadyExistsException;
import com.slinger.bodygoals.ui.dtos.YearlySummaryDto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    private final MutableLiveData<Date> selectedDate = new MutableLiveData<>(Calendar.getInstance().getTime());

    private final MutableLiveData<List<Goal>> userGoals = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Date> sessionDate = new MutableLiveData<>(Calendar.getInstance().getTime());

    private final MutableLiveData<YearlySummaryDto> yearlySummaryDtoMutableLiveData =
            new MutableLiveData<>(YearlySummaryDto.EMPTY);

    private final MutableLiveData<Goal> selectedGoal = new MutableLiveData<>(Goal.EMPTY);

    private final MutableLiveData<Boolean> goalEditMode = new MutableLiveData<>(false);

    private final BodyGoalDatabase database;

    private List<Session> preSavedSessions;

    public ViewModel(@NonNull Application application) {

        super(application);

        registerLiveDataObserver();

        database = Room.databaseBuilder(application, BodyGoalDatabase.class, "body-goals-database").build();
    }

    private void registerLiveDataObserver() {

        currentUser.observeForever(user -> {

            if (user == null)
                return;

            userGoals.setValue(user.getGoals());

            int year = DateUtil.getFromDate(selectedDate.getValue(), Calendar.YEAR);

            yearlySummaryDtoMutableLiveData.setValue(YearlySummaryDto.fromSessionLog(year, user.getSessionLog()));
        });
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<List<Goal>> getUserGoals() {
        return userGoals;
    }

    public LiveData<Date> getSelectedDate() {
        return selectedDate;
    }

    public LiveData<Date> getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date date) {
        sessionDate.setValue(date);
    }

    public void addGoal(@NonNull Goal goal) throws GoalAlreadyExistsException {

        User user = currentUser.getValue();

        Objects.requireNonNull(user).addGoal(goal);

        updateUser();
    }

    public void editGoal(@NonNull Goal goal) throws GoalAlreadyExistsException {

        User user = currentUser.getValue();

        Objects.requireNonNull(user).editGoal(goal);

        updateUser();
    }

    public void addSessions(List<Session> sessions) {

        if (currentUser.getValue() == null)
            return;

        for (Session session : sessions)
            currentUser.getValue().getSessionLog().logSession(session);

        updateUser();
    }

    public List<Session> getSessions(Date date) {

        User user = currentUser.getValue();

        if (user == null)
            return Lists.of();

        return user.getSessionLog().getSessionsWeekOfYear(date);
    }

    public Set<Goal> getSessionGoals(Date date) {

        User user = currentUser.getValue();

        if (user == null)
            return Sets.of();

        return user.getSessionLog().getSessionGoalsWeekOfYear(date);
    }

    public int getGoalProgress(int weekOfYear, Goal goal) {

        User user = currentUser.getValue();

        if (user == null)
            return 0;

        return user.getSessionLog().getGoalWeeklyProgress(weekOfYear, goal);
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

    public void selectPreviousDate(int field) {

        Date selectedDate = this.selectedDate.getValue();

        if (selectedDate == null)
            throw new IllegalStateException("Selected Date should never be null.");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);

        calendar.add(field, -1);

        this.selectedDate.setValue(calendar.getTime());
    }

    public void selectNextDate(int field) {

        Date currentSelection = selectedDate.getValue();

        if (currentSelection == null)
            throw new IllegalStateException("Selected Date should never be null.");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentSelection);

        calendar.add(field, 1);

        this.selectedDate.setValue(calendar.getTime());
    }

    public LiveData<YearlySummaryDto> getYearlySummaryDtoMutableLiveData() {
        return yearlySummaryDtoMutableLiveData;
    }

    public LiveData<Goal> getSelectedGoal() {
        return selectedGoal;
    }

    public void selectGoal(Goal goal) {
        selectedGoal.setValue(goal);
    }

    public void clearSelectGoal() {
        selectedGoal.setValue(Goal.EMPTY);
    }

    public LiveData<Boolean> getGoalEditMode() {
        return goalEditMode;
    }

    public void enableGoalEditMode() {
        goalEditMode.setValue(true);
    }

    public void disableGoalEditMode() {
        goalEditMode.setValue(false);
    }
}