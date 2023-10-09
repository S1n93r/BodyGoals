package com.slinger.bodygoals.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.model.Goal;

import androidx.annotation.Nullable;

/* TODO: Can be removed if component_goal.xml works. */
public class GoalComponent extends RelativeLayout {

    private Goal goal;

    private TextView textView;

    private ImageView imageView;

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
        imageView = innerView.findViewById(R.id.button_disable_delete);

        this.addView(innerView);
    }

    public void setGoal(Goal goal) {

        this.goal = goal;

        textView.setText(goal.getName());
    }

    public void registerGoalDisableOrDeactivationListener(Runnable runnable) {

        imageView.setOnClickListener(view -> {

            runnable.run();

            if(!goal.isActive())
                textView.setTextColor(Color.GRAY);
            else
                textView.setTextColor(Color.BLACK);
        });
    }
}