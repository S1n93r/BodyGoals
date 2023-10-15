package com.slinger.bodygoals.model;

public class Progress {

    private final int max;
    private int current;

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

    public int getCurrentPercent() {

        double percent = Math.round((double) current / (double) max * 100.0);

        return (int) percent;
    }

    public void increaseCurrent() {

        if (current + 1 > max)
            throw new IllegalStateException("Current should not be bigger then max. Check usage.");

        current++;
    }

    public ProgressStatus getProgressStatus() {

        if (current == 0)
            return ProgressStatus.EMPTY;

        if (current == max)
            return ProgressStatus.DONE;

        return ProgressStatus.IN_PROGRESS;
    }
}