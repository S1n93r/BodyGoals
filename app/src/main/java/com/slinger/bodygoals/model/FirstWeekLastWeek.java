package com.slinger.bodygoals.model;

public class FirstWeekLastWeek {

    private final int first;
    private final int last;

    private FirstWeekLastWeek(int first, int last) {
        this.first = first;
        this.last = last;
    }

    public static FirstWeekLastWeek of(int start, int end) {
        return new FirstWeekLastWeek(start, end);
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }
}