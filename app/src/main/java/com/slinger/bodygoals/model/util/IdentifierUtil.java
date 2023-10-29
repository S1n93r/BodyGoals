package com.slinger.bodygoals.model.util;

import com.slinger.bodygoals.ui.dtos.Identifieable;

import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public final class IdentifierUtil {

    private IdentifierUtil() {
        /* Util */
    }

    public static int getNextId(List<? extends Identifieable> identifieableList) {

        int maxId = 0;

        for (int id : StreamSupport.stream(identifieableList).map(identifieable -> identifieable.getIdentifier().getId()).collect(Collectors.toSet()))
            if (id > maxId)
                maxId = id;

        return maxId + 1;
    }
}