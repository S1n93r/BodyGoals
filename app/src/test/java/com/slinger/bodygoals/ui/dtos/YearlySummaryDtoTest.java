package com.slinger.bodygoals.ui.dtos;

import static org.junit.Assert.assertEquals;

import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.Session;
import com.slinger.bodygoals.model.SessionLog;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class YearlySummaryDtoTest {

    private final Calendar calendar = Calendar.getInstance();

    @Test
    public void test() {

        Date today = Calendar.getInstance().getTime();

        Goal pushPull = new Goal("Push / Pull", 3, today);
        Goal legs = new Goal("legs", 3, today);
        Goal shoulders = new Goal("shoulders", 3, today);

        SessionLog sessionLog = new SessionLog();

        sessionLog.logSession(new Session(pushPull, today));
        sessionLog.logSession(new Session(legs, today));
        sessionLog.logSession(new Session(shoulders, today));
        sessionLog.logSession(new Session(pushPull, today));

        YearlySummaryDto sut = YearlySummaryDto.fromSessionLog(2023, sessionLog);

        calendar.setTime(today);

//        assertEquals(50, sut.getMonthlyProgress(calendar.get(Calendar.MONTH)).getMonth());
        assertEquals(50, sut.getMonthlyProgress(calendar.get(Calendar.MONTH)).getProgress());
    }
}