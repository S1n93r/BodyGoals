package com.slinger.bodygoals.ui.components;

import android.content.Context;
import android.util.AttributeSet;

import com.slinger.bodygoals.model.Goal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GoalCheckBox extends androidx.appcompat.widget.AppCompatCheckBox {

    private Goal goal;

    public GoalCheckBox(@NonNull Context context) {
        super(context);
    }

    public GoalCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GoalCheckBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {

        setText(goal.getName());

        this.goal = goal;
    }
}
