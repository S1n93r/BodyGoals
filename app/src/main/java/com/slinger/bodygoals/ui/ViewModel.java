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
import com.slinger.bodygoals.ui.exercises.ExerciseDto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java8.util.Lists;
import java8.util.Sets;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import lombok.Getter;

public class ViewModel extends AndroidViewModel {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Map<UserIdentifier, User> userMap = new HashMap<>();

    private final MutableLiveData<UserDto> currentUser = new MutableLiveData<>(UserDto.EMPTY);

    private final MutableLiveData<LocalDate> selectedDate = new MutableLiveData<>(LocalDate.now());

    private final MutableLiveData<YearlySummaryDto> yearlySummaryDtoMutableLiveData =
            new MutableLiveData<>(YearlySummaryDto.EMPTY);

    @Getter
    private final MutableLiveData<GoalDto> selectedGoal = new MutableLiveData<>(GoalDto.EMPTY);

    private final MutableLiveData<Boolean> goalEditMode = new MutableLiveData<>(false);

    @Getter
    private final MutableLiveData<ExerciseDto> selectedExercise = new MutableLiveData<>();

    private final BodyGoalDatabase database;

    public ViewModel(@NonNull Application application) {

        super(application);

        registerLiveDataObserver();

        database = Room.databaseBuilder(application, BodyGoalDatabase.class, "body-goals-database").build();
    }

    private void registerLiveDataObserver() {

        currentUser.observeForever(userDto -> {

            /* TODO: Turn on again if yearl summary is re-implemented. */
//            int year = DateUtil.getFromDate(selectedDate.getValue(), Calendar.YEAR);
//
//            yearlySummaryDtoMutableLiveData.setValue(YearlySummaryDto.fromSessionLog(year, user.getSessionLog()));
        });
    }

    public LiveData<UserDto> getCurrentUser() {
        return currentUser;
    }

    public LiveData<LocalDate> getSelectedDate() {
        return selectedDate;
    }

    public void addGoal(@NonNull GoalDto goalDto) throws GoalAlreadyExistsException {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        Objects.requireNonNull(user).addGoalWithNewId(goalDto.getName(), goalDto.getFrequency(), goalDto.getCreationDate(), goalDto.getMuscleGroupsCopy());

        currentUser.setValue(UserDto.from(user));

        /* TODO: Do on app closed. */
        saveUserToDatabase(user);
    }

    public void editGoal(@NonNull GoalDto goalDto) throws GoalAlreadyExistsException {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        Objects.requireNonNull(user).editGoal(Objects.requireNonNull(selectedGoal.getValue()).getGoalIdentifier(),
                goalDto.getName(), goalDto.getFrequency(), goalDto.getCreationDate(), goalDto.getMuscleGroupsCopy());

        currentUser.setValue(UserDto.from(user));

        /* TODO: Do on app closed. */
        saveUserToDatabase(user);
    }

    public void addSessions(List<SessionDto> sessionDtos) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        StreamSupport.stream(sessionDtos).map(SessionDto::to)
                .forEach(session -> Objects.requireNonNull(user).getSessionLog().logSession(session.getGoal(), session.getDate()));

        currentUser.setValue(UserDto.from(Objects.requireNonNull(user)));

        /* TODO: Do on app closed. */
        saveUserToDatabase(user);
    }

    public List<SessionDto> getSessionsWeekOfYear(LocalDate date) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        if (user == null)
            return Lists.of();

        return StreamSupport.stream(user.getSessionLog().getSessionsWeekOfYear(date))
                .map(SessionDto::from)
                .collect(Collectors.toList());
    }

    public Set<GoalDto> getSessionGoals(LocalDate date) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        if (user == null)
            return Sets.of();

        Set<Goal> goalsFromUserYear = user.getSessionLog().getSessionGoalsWeekOfYear(date);

        return StreamSupport.stream(goalsFromUserYear).map(GoalDto::from).collect(Collectors.toSet());
    }

    public int getGoalProgress(LocalDate date, GoalDto goalDto) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        return Objects.requireNonNull(user).getSessionLog().getGoalWeeklyProgress(date, goalDto.to());
    }

    public int getSessionsLogged(LocalDate date, GoalDto goalDto) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        return Objects.requireNonNull(user).getSessionLog().getNumberOfSessionsLoggedWeekOfYear(date, goalDto.getGoalIdentifier());
    }

    private void saveUserToDatabase(User user) {

        if (database != null)
            executor.execute(() -> database.userDao().insertAll(user));
    }

    public void loadUser() {

        executor.execute(() -> {

            User user = database.userDao().findByName(123);

            if (user == null)
                user = new User();

            userMap.put(UserIdentifier.of(user.getUserId()), user);

            UserDto userDto = UserDto.from(user);

            handler.post(() -> currentUser.setValue(userDto));
        });
    }

    public void deleteGoal(GoalDto goalDto) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        Objects.requireNonNull(user).removeGoal(goalDto.getGoalIdentifier());

        currentUser.setValue(UserDto.from(user));

        saveUserToDatabase(user);
    }

    public void removeLogEntry(SessionDto sessionDto) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        Objects.requireNonNull(user).getSessionLog().removeLogSession(sessionDto.getSessionIdentifier());

        currentUser.setValue(UserDto.from(user));

        saveUserToDatabase(user);
    }

    public void selectPreviousWeekOfYear() {

        LocalDate selectedDate = this.selectedDate.getValue();

        if (selectedDate == null)
            throw new IllegalStateException("Selected Date should never be null.");

        this.selectedDate.setValue(selectedDate.minusWeeks(1));
    }

    public void selectNextWeekOfYear() {

        LocalDate selectedDate = this.selectedDate.getValue();

        if (selectedDate == null)
            throw new IllegalStateException("Selected Date should never be null.");

        this.selectedDate.setValue(selectedDate.plusWeeks(1));
    }

    public void selectPreviousYear() {

        LocalDate selectedDate = this.selectedDate.getValue();

        if (selectedDate == null)
            throw new IllegalStateException("Selected Date should never be null.");

        selectedDate.minusYears(1);

        this.selectedDate.setValue(selectedDate.minusYears(1));
    }

    public void selectNextYear() {

        LocalDate selectedDate = this.selectedDate.getValue();

        if (selectedDate == null)
            throw new IllegalStateException("Selected Date should never be null.");

        this.selectedDate.setValue(selectedDate.plusYears(1));
    }

    public Map<MuscleGroup, Progress> getProgressPerMuscleGroup(LocalDate date) {

        UserDto userDto = currentUser.getValue();

        User user = userMap.get(Objects.requireNonNull(userDto).getUserIdentifier());

        return Objects.requireNonNull(user).getSessionLog().progressPerMuscleGroup(date);
    }

    public LiveData<YearlySummaryDto> getYearlySummaryDtoMutableLiveData() {
        return yearlySummaryDtoMutableLiveData;
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