package com.slinger.bodygoals.ui.dtos;

import com.slinger.bodygoals.model.SessionLog;

import java.util.HashMap;
import java.util.Map;

public class YearlySummaryDto {

    public static YearlySummaryDto EMPTY = of(1990, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0);

    private final int year;

    private final Map<Month, MonthlySummaryDto> monthlySummaryMap;

    public YearlySummaryDto(int year, Map<Month, MonthlySummaryDto> monthlySummaryMap) {
        this.year = year;
        this.monthlySummaryMap = monthlySummaryMap;
    }

    public static YearlySummaryDto of(int year, int jan, int feb, int mar, int apr, int may, int jun,
                                      int jul, int aug, int sep, int oct, int nov, int dec) {

        Map<Month, MonthlySummaryDto> monthlySummaryMap = new HashMap<>();

        monthlySummaryMap.put(Month.JANUARY, MonthlySummaryDto.of(year, Month.JANUARY, jan));
        monthlySummaryMap.put(Month.FEBRUARY, MonthlySummaryDto.of(year, Month.FEBRUARY, feb));
        monthlySummaryMap.put(Month.MARCH, MonthlySummaryDto.of(year, Month.MARCH, mar));
        monthlySummaryMap.put(Month.APRIL, MonthlySummaryDto.of(year, Month.APRIL, apr));
        monthlySummaryMap.put(Month.MAY, MonthlySummaryDto.of(year, Month.MAY, may));
        monthlySummaryMap.put(Month.JUNE, MonthlySummaryDto.of(year, Month.JUNE, jun));
        monthlySummaryMap.put(Month.JULY, MonthlySummaryDto.of(year, Month.JULY, jul));
        monthlySummaryMap.put(Month.AUGUST, MonthlySummaryDto.of(year, Month.AUGUST, aug));
        monthlySummaryMap.put(Month.SEPTEMBER, MonthlySummaryDto.of(year, Month.SEPTEMBER, sep));
        monthlySummaryMap.put(Month.OCTOBER, MonthlySummaryDto.of(year, Month.OCTOBER, oct));
        monthlySummaryMap.put(Month.NOVEMBER, MonthlySummaryDto.of(year, Month.NOVEMBER, nov));
        monthlySummaryMap.put(Month.DECEMBER, MonthlySummaryDto.of(year, Month.DECEMBER, dec));

        return new YearlySummaryDto(year, monthlySummaryMap);
    }

    /* TODO: Should later be "from sessionLogDto" */
    public static YearlySummaryDto fromSessionLog(SessionLog sessionLog) {
        /* TODO: We need more support classes to implement this. */
        return null;
    }

    public MonthlySummaryDto getMonthlyProgress(Month month) {
        return monthlySummaryMap.get(month);
    }
}