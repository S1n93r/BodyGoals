package com.slinger.bodygoals.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.model.Goal;

public class GoalEntry extends RelativeLayout {

    private Goal goal;

    private TextView textView;

    private ImageView imageViewEdit;
    private ImageView imageViewDelete;
    private ImageView imageViewConfirmDelete;
    private ImageView imageViewCancelDelete;

    private int originalTextColor;

    public GoalEntry(Context context) {

        super(context);

        configureComponents(context);
    }

    public GoalEntry(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        configureComponents(context);
    }

    public GoalEntry(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        configureComponents(context);
    }

    public GoalEntry(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        configureComponents(context);
    }


    private void configureComponents(Context context) {

        View innerView = inflate(context, R.layout.component_goal_entry, null);

        textView = innerView.findViewById(R.id.overview_entry_text);

        originalTextColor = textView.getCurrentTextColor();

        imageViewEdit = innerView.findViewById(R.id.button_edit);

        imageViewDelete = innerView.findViewById(R.id.button_delete);
        imageViewDelete.setOnClickListener(v -> {
            imageViewEdit.setVisibility(GONE);
            imageViewDelete.setVisibility(GONE);
            imageViewConfirmDelete.setVisibility(VISIBLE);
            imageViewCancelDelete.setVisibility(VISIBLE);
        });

        imageViewConfirmDelete = innerView.findViewById(R.id.button_confirm_delete);

        imageViewCancelDelete = innerView.findViewById(R.id.button_cancel_delete);
        imageViewCancelDelete.setOnClickListener(v -> {
            imageViewEdit.setVisibility(VISIBLE);
            imageViewDelete.setVisibility(VISIBLE);
            imageViewConfirmDelete.setVisibility(GONE);
            imageViewCancelDelete.setVisibility(GONE);
        });

        this.addView(innerView);
    }

    public void setGoal(Goal goal) {

        this.goal = goal;

        textView.setText(goal.getName());
    }

    public void registerEditGoalRunner(Runnable runnable) {
        imageViewEdit.setOnClickListener(view -> runnable.run());
    }

    public void registerDeleteGoalRunner(Runnable runnable) {
        imageViewConfirmDelete.setOnClickListener(view -> runnable.run());
    }
}