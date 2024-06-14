package com.slinger.bodygoals.ui.dtos;

import androidx.annotation.NonNull;

import com.slinger.bodygoals.model.User;
import com.slinger.bodygoals.model.UserIdentifier;
import com.slinger.bodygoals.ui.exercises.ExerciseDto;

import java.util.List;
import java.util.Objects;

import java8.util.Lists;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import lombok.Getter;

@Getter
public class UserDto {

    public static UserDto EMPTY = UserDto.of(UserIdentifier.of(123), Lists.of(), Lists.of(), Lists.of());

    /* Getters */
    private final UserIdentifier userIdentifier;

    private final List<SessionDto> loggedSessions;

    private final List<GoalDto> goalDtos;

    private final List<ExerciseDto> exerciseDtos;

    private UserDto(@NonNull UserIdentifier userIdentifier, @NonNull List<SessionDto> loggedSessions,
                    @NonNull List<GoalDto> goalDtos, @NonNull List<ExerciseDto> exerciseDtos) {

        this.userIdentifier = userIdentifier;
        this.loggedSessions = loggedSessions;
        this.goalDtos = goalDtos;
        this.exerciseDtos = exerciseDtos;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDto userDto = (UserDto) o;

        return userIdentifier.equals(userDto.userIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIdentifier);
    }

    public static UserDto of(@NonNull UserIdentifier userId, @NonNull List<SessionDto> loggedSessions,
                             @NonNull List<GoalDto> goals, @NonNull List<ExerciseDto> exerciseDtos) {
        return new UserDto(userId, loggedSessions, goals, exerciseDtos);
    }

    public static UserDto from(User user) {

        List<SessionDto> sessionDtos = StreamSupport.stream(user.getSessionLog().getLoggedSessions()).map(SessionDto::from).collect(Collectors.toList());
        List<GoalDto> goalDtos = StreamSupport.stream(user.getGoals()).map(GoalDto::from).collect(Collectors.toList());
        List<ExerciseDto> exerciseDtos = StreamSupport.stream(user.getExercises()).map(ExerciseDto::from).collect(Collectors.toList());

        return UserDto.of(UserIdentifier.of(user.getUserId()), sessionDtos, goalDtos, exerciseDtos);
    }

    public User to() {

        User user = new User();

        goalDtos.forEach(goal -> user.addGoalWithNewId(goal.getName(), goal.getFrequency(), goal.getCreationDate(), goal.getMuscleGroupsCopy()));
        loggedSessions.forEach(session -> user.getSessionLog().logSession(session.getGoal().to(), session.getDate()));

        return user;
    }

}