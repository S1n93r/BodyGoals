package com.slinger.bodygoals.model;

import java.util.Objects;

public class UserIdentifier {

    public static UserIdentifier DEFAULT = UserIdentifier.of(-1);

    private final int id;

    private UserIdentifier(int id) {
        this.id = id;
    }

    public static UserIdentifier of(int id) {
        return new UserIdentifier(id);
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        UserIdentifier that = (UserIdentifier) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}