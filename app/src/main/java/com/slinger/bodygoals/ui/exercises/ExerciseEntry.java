package com.slinger.bodygoals.ui.exercises;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.slinger.bodygoals.R;

public class ExerciseEntry extends RelativeLayout {

    private TextView exerciseName;
    private TextView trend;

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

        exerciseName = innerView.findViewById(R.id.exercise_name);

        trend = innerView.findViewById(R.id.trend);

        this.addView(innerView);
    }

    public void setExerciseDto(ExerciseDto exerciseDto) {

        String text = String.format("%s (%s)", exerciseDto.getType(), exerciseDto.getVariant());

        exerciseName.setText(text);

        double trend = exerciseDto.getTrend();

        int colorIdRed = ContextCompat.getColor(getContext(), R.color.red);
        int colorIdBlack = ContextCompat.getColor(getContext(), R.color.black);
        int colorIdGreen = ContextCompat.getColor(getContext(), R.color.green);

        if (trend < 0)
            exerciseName.setTextColor(colorIdRed);
        else if (trend == 0)
            exerciseName.setTextColor(colorIdBlack);
        else
            exerciseName.setTextColor(colorIdGreen);

        exerciseName.setText(String.format("%s %%", trend));
    }
}