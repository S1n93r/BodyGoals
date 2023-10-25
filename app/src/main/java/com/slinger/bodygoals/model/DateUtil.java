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

    public static FirstWeekLastWeek getFirstWeekAndLastWeekOfMonth(Date date) {

        int month = DateUtil.getFromDate(date, Calendar.MONTH);

        switch (month) {

            case 0:
                return FirstWeekLastWeek.of(1, 4);

            case 1:
                return FirstWeekLastWeek.of(5, 8);

            case 2:
                return FirstWeekLastWeek.of(9, 13);

            case 3:
                return FirstWeekLastWeek.of(14, 17);

            case 4:
                return FirstWeekLastWeek.of(18, 21);

            case 5:
                return FirstWeekLastWeek.of(22, 26);

            case 6:
                return FirstWeekLastWeek.of(27, 30);

            case 7:
                return FirstWeekLastWeek.of(31, 34);

            case 8:
                return FirstWeekLastWeek.of(35, 39);

            case 9:
                return FirstWeekLastWeek.of(40, 43);

            case 10:
                return FirstWeekLastWeek.of(44, 47);

            case 11:
                return FirstWeekLastWeek.of(48, 52);

            default:
                throw new IllegalStateException("Unexpected value: " + month);
        }
    }
}
