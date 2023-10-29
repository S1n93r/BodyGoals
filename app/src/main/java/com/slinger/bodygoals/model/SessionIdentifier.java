package com.slinger.bodygoals.model;

import java.util.Objects;

public class SessionIdentifier implements Identifier {

    public static SessionIdentifier DEFAULT = SessionIdentifier.of(-1);

    private final int id;

    private SessionIdentifier(int id) {
        this.id = id;
    }

    public static SessionIdentifier of(int id) {
        return new SessionIdentifier(id);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        SessionIdentifier that = (SessionIdentifier) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}