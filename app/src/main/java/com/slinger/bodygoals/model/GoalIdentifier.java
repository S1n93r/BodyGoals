package com.slinger.bodygoals.model;

import java.util.Objects;

public class GoalIdentifier {

    public static GoalIdentifier DEFAULT = GoalIdentifier.of(-1);

    private final int id;

    private GoalIdentifier(int id) {
        this.id = id;
    }

    public static GoalIdentifier of(int id) {
        return new GoalIdentifier(id);
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

        GoalIdentifier that = (GoalIdentifier) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}