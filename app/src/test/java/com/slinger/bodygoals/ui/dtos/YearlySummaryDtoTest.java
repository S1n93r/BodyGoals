package com.slinger.bodygoals.ui.dtos;

import static org.junit.Assert.assertEquals;

import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.GoalIdentifier;
import com.slinger.bodygoals.model.log.SessionLog;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;

public class YearlySummaryDtoTest {

    private final Calendar calendar = Calendar.getInstance();

    @Test
    public void yearlySummaryDtoIsCorrectlyCreatedFromSessionLog() {

        LocalDate today = LocalDate.now();

        Goal pushPull = new Goal(GoalIdentifier.DEFAULT, "Push / Pull", 3, today);
        Goal legs = new Goal(GoalIdentifier.DEFAULT, "legs", 3, today);
        Goal shoulders = new Goal(GoalIdentifier.DEFAULT, "shoulders", 3, today);

        SessionLog sessionLog = new SessionLog();

        sessionLog.logSession(pushPull, today);
        sessionLog.logSession(legs, today);
        sessionLog.logSession(shoulders, today);
        sessionLog.logSession(pushPull, today);

        YearlySummaryDto sut = YearlySummaryDto.fromSessionLog(2023, sessionLog);

//        assertEquals(50, sut.getMonthlyProgress(calendar.get(Calendar.MONTH)).getMonth());
        assertEquals(44, sut.getMonthlyProgress(today.getMonthValue()).getProgress());
    }
}