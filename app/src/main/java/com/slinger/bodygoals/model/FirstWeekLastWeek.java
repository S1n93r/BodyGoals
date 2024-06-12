package com.slinger.bodygoals.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class FirstWeekLastWeek {

    int first;
    int last;
    
}