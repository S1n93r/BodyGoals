package com.slinger.bodygoals.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class UserIdentifier {

    public static UserIdentifier DEFAULT = UserIdentifier.of(-1);

    int id;
}