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
import com.slinger.bodygoals.ui.dtos.GoalDto;
import com.slinger.bodygoals.ui.dtos.SessionDto;
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
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class ViewModel extends AndroidViewModel {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    private final MutableLiveData<User> currentUser =
            new MutableLiveData<>(new User());

    private final MutableLiveData<Date> selectedDate = new MutableLiveData<>(Calendar.getInstance().getTime());

    private final MutableLiveData<List<GoalDto>> userGoals = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Date> sessionDate = new MutableLiveData<>(Calendar.getInstance().getTime());

    private final MutableLiveData<YearlySummaryDto> yearlySummaryDtoMutableLiveData =
            new MutableLiveData<>(YearlySummaryDto.EMPTY);

    private final MutableLiveData<GoalDto> selectedGoal = new MutableLiveData<>(GoalDto.EMPTY);

    private final MutableLiveData<Boolean> goalEditMode = new MutableLiveData<>(false);

    private final BodyGoalDatabase database;

    private List<SessionDto> preSavedSessions;

    public ViewModel(@NonNull Application application) {

        super(application);

        registerLiveDataObserver();

        database = Room.databaseBuilder(application, BodyGoalDatabase.class, "body-goals-database").build();
    }

    private void registerLiveDataObserver() {

        currentUser.observeForever(user -> {

            if (user == null)
                return;

            List<GoalDto> goalDtos = StreamSupport.stream(user.getGoals())
                    .map(GoalDto::from)
                    .collect(Collectors.toList());

            userGoals.setValue(goalDtos);

            int year = DateUtil.getFromDate(selectedDate.getValue(), Calendar.YEAR);

            yearlySummaryDtoMutableLiveData.setValue(YearlySummaryDto.fromSessionLog(year, user.getSessionLog()));
        });
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<List<GoalDto>> getUserGoals() {
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

    public void addGoal(@NonNull GoalDto goalDto) throws GoalAlreadyExistsException {

        User user = currentUser.getValue();

        Objects.requireNonNull(user).addGoal(goalDto.getName(), goalDto.getFrequency(), goalDto.getCreationDate(), goalDto.getMuscleGroupsCopy());

        updateUser();
    }

    /* FIXME: Saving an edited goal does not do anything. */
    public void editGoal(@NonNull GoalDto goalDto) throws GoalAlreadyExistsException {

        User user = currentUser.getValue();

        Objects.requireNonNull(user).editGoal(Objects.requireNonNull(selectedGoal.getValue()).getGoalIdentifier(),
                goalDto.getName(), goalDto.getFrequency(), goalDto.getMuscleGroupsCopy());

        updateUser();
    }

    public void addSessions(List<SessionDto> sessionDtos) {

        if (currentUser.getValue() == null)
            return;

        StreamSupport.stream(sessionDtos).map(SessionDto::to).forEach(session -> currentUser.getValue().getSessionLog().logSession(session));

        updateUser();
    }

    public List<Session> getSessions(Date date) {

        User user = currentUser.getValue();

        if (user == null)
            return Lists.of();

        return user.getSessionLog().getSessionsWeekOfYear(date);
    }

    public Set<GoalDto> getSessionGoals(Date date) {

        User user = currentUser.getValue();

        if (user == null)
            return Sets.of();

        Set<Goal> goalsFromUserYear = user.getSessionLog().getSessionGoalsWeekOfYear(date);

        return StreamSupport.stream(goalsFromUserYear).map(GoalDto::from).collect(Collectors.toSet());
    }

    public int getGoalProgress(int weekOfYear, GoalDto goalDto) {

        User user = currentUser.getValue();

        if (user == null)
            return 0;

        return user.getSessionLog().getGoalWeeklyProgress(weekOfYear, goalDto.to());
    }

    public int getSessionsLogged(int weekOfYear, GoalDto goalDto) {

        User user = currentUser.getValue();

        if (user == null)
            return 0;

        if (user.getSessionLog() == null)
            return 0;

        return user.getSessionLog().getSessionsLogged(weekOfYear, goalDto.to());
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

    public void deleteGoal(GoalDto goalDto) {

        User user = currentUser.getValue();

        if (user == null)
            return;

        user.removeGoal(goalDto.to());

        updateUser();
    }

    public void removeLogEntry(Session session) {

        if (currentUser.getValue() == null)
            return;

        currentUser.getValue().getSessionLog().removeLogSession(session);

        updateUser();
    }

    public List<SessionDto> getPreSavedSessions() {
        return preSavedSessions;
    }

    public void setPreSavedSessions(List<SessionDto> preSavedSessions) {
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

    public LiveData<GoalDto> getSelectedGoal() {
        return selectedGoal;
    }

    public void selectGoal(GoalDto goal) {
        selectedGoal.setValue(goal);
    }

    public void clearSelectGoal() {
        selectedGoal.setValue(GoalDto.EMPTY);
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