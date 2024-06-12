package com.slinger.bodygoals.ui.components;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.slinger.bodygoals.ui.dtos.GoalDto;

import lombok.Getter;

@Getter
public class GoalCheckBox extends androidx.appcompat.widget.AppCompatCheckBox {

    private GoalDto goal;

    public GoalCheckBox(@NonNull Context context) {
        super(context);
    }

    public GoalCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GoalCheckBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setGoal(GoalDto goal) {

        setText(goal.getName());

        this.goal = goal;
    }
}
