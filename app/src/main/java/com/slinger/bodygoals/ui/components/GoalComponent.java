package com.slinger.bodygoals.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.model.Goal;

import androidx.annotation.Nullable;

/* TODO: Can be removed if component_goal.xml works. */
public class GoalComponent extends LinearLayout {

    private Goal goal;

    private final TextView textView;

    private final ImageView imageView;

    public GoalComponent(Context context) {

        super(context);

        textView = new TextView(context);
        imageView = new ImageView(context);

        configureComponents();
    }

    public GoalComponent(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        textView = new TextView(context, attrs);
        imageView = new ImageView(context, attrs);

        configureComponents();
    }

    public GoalComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        textView = new TextView(context, attrs, defStyleAttr);
        imageView = new ImageView(context, attrs, defStyleAttr);

        configureComponents();
    }

    public GoalComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        textView = new TextView(context, attrs, defStyleAttr, defStyleRes);
        imageView = new ImageView(context, attrs, defStyleAttr, defStyleRes);

        configureComponents();
    }

    private void configureComponents() {

        textView.setMinWidth(200);
        textView.setMaxWidth(200);

        textView.setMinHeight(24);
        textView.setMaxHeight(24);

        textView.setTextSize(18);

        imageView.setMinimumHeight(24);
        imageView.setMaxHeight(24);

        imageView.setImageResource(R.drawable.ic_delete);
    }

    public void setGoal(Goal goal) {

        this.goal = goal;

        textView.setText(goal.getName());
    }

    public void registerOnClickListener(Runnable runnable) {
        imageView.setOnClickListener(view -> runnable.run());
    }
}