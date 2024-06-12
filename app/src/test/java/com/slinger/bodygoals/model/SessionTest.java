package com.slinger.bodygoals.model;

import static org.junit.Assert.assertEquals;

import com.slinger.bodygoals.model.log.Session;
import com.slinger.bodygoals.model.log.SessionIdentifier;
import com.slinger.bodygoals.model.util.DateUtil;

import org.junit.Test;

import java.time.LocalDate;

public class SessionTest {

    @Test
    public void test() {

        LocalDate today = LocalDate.now();

        Goal goal = new Goal(GoalIdentifier.of(11), "Push", 3, today);

        Session sut = new Session(SessionIdentifier.of(1), goal, today);

        assertEquals(1, sut.getSessionIdentifier().getId());
        assertEquals(1, sut.getSessionIdentifier().getId());

        assertEquals(goal, sut.getGoal());

        assertEquals(today, sut.getDate());
        assertEquals(DateUtil.getWeekOfYear(today), sut.getWeekOfYear());
    }
}