package com.slinger.bodygoals.ui.dtos;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class MonthlySummaryDto {

    int year;
    int month;
    int progress;

}