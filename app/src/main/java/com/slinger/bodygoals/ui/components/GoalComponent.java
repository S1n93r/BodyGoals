package com.slinger.bodygoals.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.model.Goal;

import androidx.annotation.Nullable;

/* TODO: Can be removed if component_goal.xml works. */
public class GoalComponent extends RelativeLayout {

    private Goal goal;

    private TextView textView;

    private ImageView imageViewEnable;
    private ImageView imageViewDisable;
    private ImageView imageViewDelete;

    private int originalTextColor;

    public GoalComponent(Context context) {

        super(context);

        configureComponents(context);
    }

    public GoalComponent(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        configureComponents(context);
    }

    public GoalComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        configureComponents(context);
    }

    public GoalComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        configureComponents(context);
    }



    private void configureComponents(Context context) {

        View innerView = inflate(context, R.layout.component_goal,  null);

        textView = innerView.findViewById(R.id.goal_name_text);

        originalTextColor = textView.getCurrentTextColor();

        imageViewEnable = innerView.findViewById(R.id.button_enable);
        imageViewDisable = innerView.findViewById(R.id.button_disable);
        imageViewDelete = innerView.findViewById(R.id.button_delete);

        this.addView(innerView);
    }

    public void setGoal(Goal goal) {

        this.goal = goal;

        updateStatus(this.goal.isActive());

        textView.setText(goal.getName());
    }

    public void registerDisableGoalRunner(Runnable runnable) {

        imageViewDisable.setOnClickListener(view -> {

            runnable.run();

            updateStatus(goal.isActive());
        });
    }

    public void registerEnableGoalRunner(Runnable runnable) {

        imageViewEnable.setOnClickListener(view -> {

            runnable.run();

            updateStatus(goal.isActive());
        });
    }

    public void registerDeleteGoalRunner(Runnable runnable) {

        imageViewDelete.setOnClickListener(view -> {

            runnable.run();

            updateStatus(goal.isActive());
        });
    }

    private void updateStatus(boolean isActive) {

        if(!isActive) {

            textView.setTextColor(Color.GRAY);

            imageViewDelete.setVisibility(VISIBLE);
            imageViewEnable.setVisibility(VISIBLE);
            imageViewDisable.setVisibility(GONE);

        } else {

            textView.setTextColor(originalTextColor);

            imageViewDelete.setVisibility(GONE);
            imageViewEnable.setVisibility(GONE);
            imageViewDisable.setVisibility(VISIBLE);
        }
    }
}