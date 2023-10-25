package com.slinger.bodygoals.ui.dtos;

import com.slinger.bodygoals.model.SessionLog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class YearlySummaryDto {

    public static YearlySummaryDto EMPTY = of(1990, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0);

    private final int year;

    private final Map<Integer, MonthlySummaryDto> monthlySummaryMap;

    public YearlySummaryDto(int year, Map<Integer, MonthlySummaryDto> monthlySummaryMap) {
        this.year = year;
        this.monthlySummaryMap = monthlySummaryMap;
    }

    public static YearlySummaryDto of(int year, int jan, int feb, int mar, int apr, int may, int jun,
                                      int jul, int aug, int sep, int oct, int nov, int dec) {

        Map<Integer, MonthlySummaryDto> monthlySummaryMap = new HashMap<>();

        monthlySummaryMap.put(Calendar.JANUARY, MonthlySummaryDto.of(year, Calendar.JANUARY, jan));
        monthlySummaryMap.put(Calendar.FEBRUARY, MonthlySummaryDto.of(year, Calendar.FEBRUARY, feb));
        monthlySummaryMap.put(Calendar.MARCH, MonthlySummaryDto.of(year, Calendar.MARCH, mar));
        monthlySummaryMap.put(Calendar.APRIL, MonthlySummaryDto.of(year, Calendar.APRIL, apr));
        monthlySummaryMap.put(Calendar.MAY, MonthlySummaryDto.of(year, Calendar.MAY, may));
        monthlySummaryMap.put(Calendar.JUNE, MonthlySummaryDto.of(year, Calendar.JUNE, jun));
        monthlySummaryMap.put(Calendar.JULY, MonthlySummaryDto.of(year, Calendar.JULY, jul));
        monthlySummaryMap.put(Calendar.AUGUST, MonthlySummaryDto.of(year, Calendar.AUGUST, aug));
        monthlySummaryMap.put(Calendar.SEPTEMBER, MonthlySummaryDto.of(year, Calendar.SEPTEMBER, sep));
        monthlySummaryMap.put(Calendar.OCTOBER, MonthlySummaryDto.of(year, Calendar.OCTOBER, oct));
        monthlySummaryMap.put(Calendar.NOVEMBER, MonthlySummaryDto.of(year, Calendar.NOVEMBER, nov));
        monthlySummaryMap.put(Calendar.DECEMBER, MonthlySummaryDto.of(year, Calendar.DECEMBER, dec));

        return new YearlySummaryDto(year, monthlySummaryMap);
    }

    /* TODO: Should later be "from sessionLogDto" */
    public static YearlySummaryDto fromSessionLog(int year, SessionLog sessionLog) {



        /* TODO: We need more support classes to implement this. */
        return null;
    }

    public MonthlySummaryDto getMonthlyProgress(int month) {
        return monthlySummaryMap.get(month);
    }
}