package com.slinger.bodygoals.ui.dtos;

import com.slinger.bodygoals.model.log.SessionLog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class YearlySummaryDto {

    public static YearlySummaryDto EMPTY = of(1990, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0);

    @Getter
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

    public static YearlySummaryDto fromSessionLog(int year, SessionLog sessionLog) {

        Map<Integer, MonthlySummaryDto> monthlySummaryMap = new HashMap<>();

        for (int iMonth = Calendar.JANUARY; iMonth <= Calendar.DECEMBER; iMonth++)
            monthlySummaryMap.put(iMonth, MonthlySummaryDto.of(year, iMonth, 0));

        Map<Integer, Integer> overallMonthlyProgresses = sessionLog.getOverallMonthlyProgresses(year);

        overallMonthlyProgresses.forEach((month, progress) ->
                monthlySummaryMap.put(month, MonthlySummaryDto.of(year, month, progress)));

        return new YearlySummaryDto(year, monthlySummaryMap);
    }

    public MonthlySummaryDto getMonthlyProgress(int month) {
        return monthlySummaryMap.get(month);
    }
}