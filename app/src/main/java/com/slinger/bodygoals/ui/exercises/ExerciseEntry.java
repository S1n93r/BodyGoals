package com.slinger.bodygoals.ui.exercises;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.slinger.bodygoals.R;

public class ExerciseEntry extends RelativeLayout {

    private TextView textView;

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

        textView = innerView.findViewById(R.id.entry_text);

        this.addView(innerView);
    }

    public void setExerciseDto(ExerciseDto exerciseDto) {

        String text = String.format("%s (%s)", exerciseDto.getType(), exerciseDto.getVariant());

        textView.setText(text);
    }
}