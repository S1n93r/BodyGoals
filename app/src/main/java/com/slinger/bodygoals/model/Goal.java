package com.slinger.bodygoals.model;

public abstract class Goal {

    private final int id;

    private final String name;

    public Goal(int id, String name) {
        this.id = id;
        this.name = name;
    }
}