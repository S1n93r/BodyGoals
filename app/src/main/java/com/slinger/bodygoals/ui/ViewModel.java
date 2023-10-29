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
import com.slinger.bodygoals.model.MuscleGroup;
import com.slinger.bodygoals.model.Progress;
import com.slinger.bodygoals.model.User;
import com.slinger.bodygoals.model.UserIdentifier;
import com.slinger.bodygoals.model.exceptions.GoalAlreadyExistsException;
import com.slinger.bodygoals.ui.dtos.GoalDto;
import com.slinger.bodygoals.ui.dtos.SessionDto;
import com.slinger.bodygoals.ui.dtos.UserDto;
import com.slinger.bodygoals.ui.dtos.YearlySummaryDto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class ViewModel extends AndroidViewModel {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Map<UserIdentifier, User> userMap = new HashMap<>();

    private final MutableLiveData<UserDto> currentUser = new MutableLiveData<>(UserDto.EMPTY);

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

        currentUser.observeForever(userDto -> {

            userGoals.setValue(userDto.getGoalDtos());

            /* TODO: Turn on again if yearl summary is re-implemented. */
//            int year = DateUtil.getFromDate(selectedDate.getValue(), Calendar.YEAR);
//
//            yearlySummaryDtoMutableLiveData.setValue(YearlySummaryDto.fromSessionLog(year, user.getSessionLog()));
        });
    }

    public LiveData<UserDto> getCurrentUser() {
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

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        Objects.requireNonNull(user).addGoal(goalDto.to());

        currentUser.setValue(UserDto.from(user));

        /* TODO: Do on app closed. */
        saveUserToDatabase(user);
    }

    public void editGoal(@NonNull GoalDto goalDto) throws GoalAlreadyExistsException {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        Objects.requireNonNull(user).editGoal(Objects.requireNonNull(selectedGoal.getValue()).getGoalIdentifier(),
                goalDto.getName(), goalDto.getFrequency(), goalDto.getMuscleGroupsCopy());

        currentUser.setValue(UserDto.from(user));

        /* TODO: Do on app closed. */
        saveUserToDatabase(user);
    }

    public void addSessions(List<SessionDto> sessionDtos) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        StreamSupport.stream(sessionDtos).map(SessionDto::to).forEach(session -> user.getSessionLog().logSession(session.getGoal(), session.getDate()));

        /* TODO: Do on app closed. */
        saveUserToDatabase(user);
    }

    public List<SessionDto> getSessionsWeekOfYear(Date date) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        return StreamSupport.stream(Objects.requireNonNull(user).getSessionLog().getSessionsWeekOfYear(date))
                .map(SessionDto::from)
                .collect(Collectors.toList());
    }

    public Set<GoalDto> getSessionGoals(Date date) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        Set<Goal> goalsFromUserYear = Objects.requireNonNull(user).getSessionLog().getSessionGoalsWeekOfYear(date);

        return StreamSupport.stream(goalsFromUserYear).map(GoalDto::from).collect(Collectors.toSet());
    }

    public int getGoalProgress(int weekOfYear, GoalDto goalDto) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        return Objects.requireNonNull(user).getSessionLog().getGoalWeeklyProgress(weekOfYear, goalDto.to());
    }

    public int getSessionsLogged(int weekOfYear, GoalDto goalDto) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        return Objects.requireNonNull(user).getSessionLog().getSessionsLogged(weekOfYear, goalDto.getGoalIdentifier());
    }

    private void saveUserToDatabase(User user) {

        if (database != null)
            executor.execute(() -> database.userDao().insertAll(user));
    }

    public void loadUser() {

        executor.execute(() -> {

            User user = database.userDao().findByName(123);

            userMap.put(UserIdentifier.of(user.getUserId()), user);

            handler.post(() -> currentUser.setValue(UserDto.from(user)));
        });
    }

    public void deleteGoal(GoalDto goalDto) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        Objects.requireNonNull(user).removeGoal(goalDto.getGoalIdentifier());

        saveUserToDatabase(user);
    }

    public void removeLogEntry(SessionDto sessionDto) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        Objects.requireNonNull(user).getSessionLog().removeLogSession(sessionDto.getSessionIdentifier());

        saveUserToDatabase(user);
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

    public Map<MuscleGroup, Progress> getProgressPerMuscleGroup(Date date) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        return Objects.requireNonNull(user).getSessionLog().progressPerMuscleGroup(date);
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