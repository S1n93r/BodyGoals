package com.slinger.bodygoals.model;

import org.junit.Assert;
import org.junit.Test;

public class CalendarWeekDayTest {

    @Test
    public void calendarWeekDayCanBeConstructedViaOf() {

        CalendarWeekIdentifier calendarWeekIdentifier = CalendarWeekIdentifier.of(39, 2023);

        /* Given, When */
        CalendarWeekDay<Integer> integerDay = CalendarWeekDay.of(calendarWeekIdentifier, "Monday", 3);
        CalendarWeekDay<Double> doubleDay = CalendarWeekDay.of(calendarWeekIdentifier, "Monday", 3d);
        CalendarWeekDay<Float> floatFay = CalendarWeekDay.of(calendarWeekIdentifier, "Monday", 3f);

        /* Then */
        Assert.assertNotNull(integerDay);

        Assert.assertEquals(Integer.class, integerDay.getLoggedSubject().getClass());
        Assert.assertEquals(Double.class, doubleDay.getLoggedSubject().getClass());
        Assert.assertEquals(Float.class, floatFay.getLoggedSubject().getClass());
    }
}