package com.slinger.bodygoals.model.exercises;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class ExerciseIdentifier {

    ExerciseType exerciseType;

    String variant;

}