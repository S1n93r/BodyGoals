package com.slinger.bodygoals.model.log;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class SessionIdentifier {

    public static SessionIdentifier DEFAULT = SessionIdentifier.of(-1);

    int id;
}