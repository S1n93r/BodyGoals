package com.slinger.bodygoals.ui.dtos;

import com.slinger.bodygoals.model.Session;
import com.slinger.bodygoals.model.SessionIdentifier;

import java.util.Date;

public class SessionDto {

    private final SessionIdentifier sessionIdentifier;
    private final GoalDto goalDto;
    private final Date date;

    public SessionDto(SessionIdentifier sessionIdentifier, GoalDto goalDto, Date date) {
        this.sessionIdentifier = sessionIdentifier;
        this.goalDto = goalDto;
        this.date = date;
    }

    public static SessionDto of(SessionIdentifier sessionIdentifier, GoalDto goalDto, Date date) {
        return new SessionDto(sessionIdentifier, goalDto, date);
    }

    public GoalDto getGoal() {
        return goalDto;
    }

    public Date getDate() {
        return date;
    }

    public SessionIdentifier getSessionIdentifier() {
        return sessionIdentifier;
    }

    public static SessionDto from(Session session) {
        return SessionDto.of(session.getSessionIdentifier(), GoalDto.from(session.getGoal()), session.getDate());
    }

    public Session to() {
        return new Session(sessionIdentifier, goalDto.to(), new Date(date.getTime()));
    }
}