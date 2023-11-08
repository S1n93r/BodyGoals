package com.slinger.bodygoals.model;

import static org.junit.Assert.assertEquals;

import com.slinger.bodygoals.model.log.Session;
import com.slinger.bodygoals.model.log.SessionIdentifier;
import com.slinger.bodygoals.model.util.DateUtil;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class SessionTest {

    @Test
    public void test() {

        Date today = Calendar.getInstance().getTime();

        Goal goal = new Goal(GoalIdentifier.of(11), "Push", 3, today);

        Session sut = new Session(SessionIdentifier.of(1), goal, today);

        assertEquals(1, sut.getIdentifier().getId());
        assertEquals(1, sut.getSessionIdentifier().getId());

        assertEquals(goal, sut.getGoal());

        assertEquals(today, sut.getDate());
        assertEquals(DateUtil.getFromDate(today, Calendar.WEEK_OF_YEAR), sut.getWeekOfYear());
    }
}