package com.slinger.bodygoals.ui.dtos;

import androidx.annotation.NonNull;

import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.Session;
import com.slinger.bodygoals.model.User;
import com.slinger.bodygoals.model.UserIdentifier;

import java.util.List;
import java.util.Objects;

public class UserDto {

    private final UserIdentifier userIdentifier;

    private final List<Session> loggedSessions;

    private final List<Goal> goals;

    private UserDto(@NonNull UserIdentifier userIdentifier, @NonNull List<Session> loggedSessions, @NonNull List<Goal> goals) {

        this.userIdentifier = userIdentifier;
        this.loggedSessions = loggedSessions;
        this.goals = goals;
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

    public static UserDto of(@NonNull UserIdentifier userId, @NonNull List<Session> loggedSessions, @NonNull List<Goal> goals) {
        return new UserDto(userId, loggedSessions, goals);
    }

    /* TODO: Convert to Util class? */
    public static UserDto from(User user) {
        return UserDto.of(UserIdentifier.of(user.getUserId()), user.getSessionLog().getLoggedSessions(), user.getGoals());
    }

    /* TODO: Convert to Util class? */
    public User to() {

        User user = new User();

        goals.forEach(goal -> user.addGoal(goal.getName(), goal.getFrequency(), goal.getCreationDate(), goal.getMuscleGroupsCopy()));
        loggedSessions.forEach(session -> user.getSessionLog().logSession(session.getGoal(), session.getDate()));

        return user;
    }

    /* Getters */
    public UserIdentifier getUserIdentifier() {
        return userIdentifier;
    }

    public List<Session> getLoggedSessions() {
        return loggedSessions;
    }

    public List<Goal> getGoals() {
        return goals;
    }
}