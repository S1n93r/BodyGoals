package com.slinger.bodygoals.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.model.Session;

import java.text.DateFormat;

public class LogEntryComponent extends RelativeLayout {

    private Session session;

    private TextView textView;

    private ImageView imageDeleteLogEntry;

    private int originalTextColor;

    public LogEntryComponent(Context context) {

        super(context);

        configureComponents(context);
    }

    public LogEntryComponent(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        configureComponents(context);
    }

    public LogEntryComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        configureComponents(context);
    }

    public LogEntryComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        configureComponents(context);
    }



    private void configureComponents(Context context) {

        View innerView = inflate(context, R.layout.component_log_entry,  null);

        textView = innerView.findViewById(R.id.overview_entry_text);

        originalTextColor = textView.getCurrentTextColor();

        imageDeleteLogEntry = innerView.findViewById(R.id.delete_log_entry_button);

        this.addView(innerView);
    }

    public void setSession(Session session) {

        this.session = session;

        DateFormat dateFormat = DateFormat.getInstance();
        String dateString = dateFormat.format(session.getDate());

        String weeklyLogEntry = String.format(getResources().getString(R.string.weekly_log_entry),
                dateString, session.getGoal().getName());

        textView.setText(weeklyLogEntry);
    }

    public void registerDisableGoalRunner(Runnable runnable) {

        imageDeleteLogEntry.setOnClickListener(view -> {
            runnable.run();
        });
    }
}