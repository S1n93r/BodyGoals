package com.slinger.bodygoals.ui.dtos;

import com.slinger.bodygoals.model.log.Session;
import com.slinger.bodygoals.model.log.SessionIdentifier;

import java.time.LocalDate;

public class SessionDto {

    private final SessionIdentifier sessionIdentifier;
    private final GoalDto goalDto;
    private final LocalDate date;

    public SessionDto(SessionIdentifier sessionIdentifier, GoalDto goalDto, LocalDate date) {
        this.sessionIdentifier = sessionIdentifier;
        this.goalDto = goalDto;
        this.date = date;
    }

    public static SessionDto of(SessionIdentifier sessionIdentifier, GoalDto goalDto, LocalDate date) {
        return new SessionDto(sessionIdentifier, goalDto, date);
    }

    public static SessionDto from(Session session) {
        return SessionDto.of(session.getSessionIdentifier(), GoalDto.from(session.getGoal()), session.getDate());
    }

    public GoalDto getGoal() {
        return goalDto;
    }

    public LocalDate getDate() {
        return date;
    }

    public SessionIdentifier getSessionIdentifier() {
        return sessionIdentifier;
    }

    public Session to() {
        return new Session(sessionIdentifier, goalDto.to(), date);
    }
}