package com.slinger.bodygoals.model;

public class User {

    private final String name;
    private final SessionLog sessionLog = new SessionLog();

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public SessionLog getSessionLog() {
        return sessionLog;
    }
}