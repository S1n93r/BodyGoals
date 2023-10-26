package com.slinger.bodygoals.ui.dtos;

import com.slinger.bodygoals.model.Session;

import java.util.Calendar;
import java.util.Date;

public class SessionDto {

    private final GoalDto goalDto;
    private final Date date;

    private SessionDto(GoalDto goalDto, Date date) {
        this.goalDto = goalDto;
        this.date = date;
    }

    public static SessionDto of(GoalDto goalDto, Date date) {
        return new SessionDto(goalDto, date);
    }

    public GoalDto getGoal() {
        return goalDto;
    }

    public Date getDate() {
        return date;
    }

    public int getWeekOfYear() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public Session to() {
        return new Session(goalDto.to(), new Date(date.getTime()));
    }
}