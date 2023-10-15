package com.slinger.bodygoals.model;

public class Progress {

    private final int max;
    private final int current;

    private Progress(int max, int current) {
        this.max = max;
        this.current = current;
    }

    public static Progress of(int max, int current) {
        return new Progress(max, current);
    }

    public int getMax() {
        return max;
    }

    public int getCurrent() {
        return current;
    }
}