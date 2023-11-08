package com.slinger.bodygoals.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.ui.dtos.SessionDto;

import java.time.format.DateTimeFormatter;

public class LogEntry extends RelativeLayout {

    private TextView textView;

    private ImageView imageDeleteLogEntry;

    public LogEntry(Context context) {

        super(context);

        configureComponents(context);
    }

    public LogEntry(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        configureComponents(context);
    }

    public LogEntry(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        configureComponents(context);
    }

    public LogEntry(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        configureComponents(context);
    }

    private void configureComponents(Context context) {

        View innerView = inflate(context, R.layout.component_log_entry, null);

        textView = innerView.findViewById(R.id.overview_entry_text);

        imageDeleteLogEntry = innerView.findViewById(R.id.button_delete);

        this.addView(innerView);
    }

    public void setSession(SessionDto sessionDto) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String dateString = sessionDto.getDate().format(formatter);

        String weeklyLogEntry = String.format(getResources().getString(R.string.weekly_log_entry),
                dateString, sessionDto.getGoal().getName());

        textView.setText(weeklyLogEntry);
    }

    public void registerDisableGoalRunner(Runnable runnable) {

        imageDeleteLogEntry.setOnClickListener(view -> {
            runnable.run();
        });
    }
}