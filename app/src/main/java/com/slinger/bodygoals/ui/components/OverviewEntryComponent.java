package com.slinger.bodygoals.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.slinger.bodygoals.R;

public class OverviewEntryComponent extends RelativeLayout {

    private TextView textView;

    private ProgressBar progressBar;

    private int originalTextColor;

    public OverviewEntryComponent(Context context) {

        super(context);

        configureComponents(context);
    }

    public OverviewEntryComponent(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        configureComponents(context);
    }

    public OverviewEntryComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        configureComponents(context);
    }

    public OverviewEntryComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        configureComponents(context);
    }

    private void configureComponents(Context context) {

        View innerView = inflate(context, R.layout.component_labeled_progress,  null);

        textView = innerView.findViewById(R.id.overview_entry_text);

        progressBar = innerView.findViewById(R.id.overview_entry_progress);

        originalTextColor = textView.getCurrentTextColor();

        this.addView(innerView);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }
}