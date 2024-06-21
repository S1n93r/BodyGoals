package com.slinger.bodygoals.ui.fragments.exercises;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.slinger.bodygoals.R;

import java.util.List;

public class ExerciseEntry extends RelativeLayout {

    private TextView exerciseNameTextView;
    private TextView unitAndRepsTextView;
    private TextView trendTextView;

    public ExerciseEntry(Context context) {

        super(context);

        configureComponents(context);
    }

    public ExerciseEntry(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        configureComponents(context);
    }

    public ExerciseEntry(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        configureComponents(context);
    }

    public ExerciseEntry(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        configureComponents(context);
    }


    private void configureComponents(Context context) {

        View innerView = inflate(context, R.layout.component_exercise_entry, null);

        exerciseNameTextView = innerView.findViewById(R.id.exercise_name);
        unitAndRepsTextView = innerView.findViewById(R.id.unit_and_reps);
        trendTextView = innerView.findViewById(R.id.trend);

        this.addView(innerView);
    }

    public void setExerciseDto(ExerciseDto exerciseDto) {

        /* Name */
        String text = String.format("%s\n(%s)", exerciseDto.getType().getName(), exerciseDto.getVariant());

        exerciseNameTextView.setText(text);

        /* Unit and Reps */
        double currentRecord = 0;

        List<Double> records = exerciseDto.getProgressHistory().toList();

        if (!records.isEmpty())
            currentRecord = records.get(0);

        unitAndRepsTextView.setText(String.format("%s %s\n(%s)", currentRecord, exerciseDto.getUnit().getName(), exerciseDto.getRepGoal()));

        /* Trend */
        double trend = exerciseDto.getTrend();

        int colorIdRed = ContextCompat.getColor(getContext(), R.color.red);
        int colorIdBlack = ContextCompat.getColor(getContext(), R.color.white);
        int colorIdGreen = ContextCompat.getColor(getContext(), R.color.green);

        if (trend < 0)
            trendTextView.setTextColor(colorIdRed);
        else if (trend == 0)
            trendTextView.setTextColor(colorIdBlack);
        else
            trendTextView.setTextColor(colorIdGreen);

        trendTextView.setText(String.format("%s %%", trend));
    }
}