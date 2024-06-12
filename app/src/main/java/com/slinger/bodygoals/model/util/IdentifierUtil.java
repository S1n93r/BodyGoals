package com.slinger.bodygoals.model.util;

import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.log.Session;

import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public final class IdentifierUtil {

    private IdentifierUtil() {
        /* Util */
    }

    public static int getNextGoalId(List<Goal> goals) {

        int maxId = 0;

        for (int id : StreamSupport.stream(goals).map(goal -> goal.getGoalIdentifier().getId()).collect(Collectors.toSet()))
            if (id > maxId)
                maxId = id;

        return maxId + 1;
    }

    public static int getNextSessionId(List<Session> sessions) {

        int maxId = 0;

        for (int id : StreamSupport.stream(sessions).map(session -> session.getSessionIdentifier().getId()).collect(Collectors.toSet()))
            if (id > maxId)
                maxId = id;

        return maxId + 1;
    }
}