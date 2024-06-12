package com.slinger.bodygoals.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class GoalIdentifier {

    public static GoalIdentifier DEFAULT = GoalIdentifier.of(-1);

    int id;
}