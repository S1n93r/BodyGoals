package com.slinger.bodygoals.ui.dtos;

public class MonthlySummaryDto {

    private final int year;
    private final Month month;
    private final int progress;

    public MonthlySummaryDto(int year, Month month, int progress) {
        this.year = year;
        this.month = month;
        this.progress = progress;
    }

    public static MonthlySummaryDto of(int year, Month month, int progress) {
        return new MonthlySummaryDto(year, month, progress);
    }

    public Month getMonth() {
        return month;
    }

    public int getProgress() {
        return progress;
    }
}