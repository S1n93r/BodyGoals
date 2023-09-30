package com.slinger.bodygoals.model;

public class User {

    private final String name;
    private final SessionLog sessionLog;

    public User(String name, SessionLog sessionLog) {
        this.name = name;
        this.sessionLog = sessionLog;
    }

    public String getName() {
        return name;
    }

    public SessionLog getSessionLog() {
        return sessionLog;
    }
}