package com.slinger.bodygoals.ui.dtos;

public class MonthlySummaryDto {

    private final int year;
    private final int month;
    private final int progress;

    public MonthlySummaryDto(int year, int month, int progress) {
        this.year = year;
        this.month = month;
        this.progress = progress;
    }

    public static MonthlySummaryDto of(int year, int month, int progress) {
        return new MonthlySummaryDto(year, month, progress);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getProgress() {
        return progress;
    }
}