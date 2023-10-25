package com.slinger.bodygoals.model;

import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    private DateUtil() {
        /* Util */
    }

    public static int getFromDate(Date date, int field) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(field);
    }

    public static boolean compareDate(Date date, Date other, int field) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        int fieldValue = calendar.get(field);

        calendar.setTime(other);
        int fieldValueOther = calendar.get(field);

        return fieldValue == fieldValueOther;
    }
}
