package com.slinger.bodygoals.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.slinger.bodygoals.R;

import java.time.LocalDate;

public class SwitchYearComponent extends ConstraintLayout {

    private LifecycleOwner lifecycleOwner;

    private TextView yearText;

    private Button toPreviousWeekButton;
    private Button toNextWeekButton;

    public SwitchYearComponent(Context context) {

        super(context);

        configureComponents(context);
    }

    public SwitchYearComponent(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        configureComponents(context);
    }

    public SwitchYearComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        configureComponents(context);
    }

    public SwitchYearComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        configureComponents(context);
    }

    private void configureComponents(Context context) {

        View innerView = inflate(context, R.layout.component_switch_year, null);

        yearText = innerView.findViewById(R.id.selected_year_text);

        toPreviousWeekButton = innerView.findViewById(R.id.previous_year_button);
        toNextWeekButton = innerView.findViewById(R.id.next_year_button);

        this.addView(innerView);
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    private void updateTexts(LocalDate date) {
        yearText.setText(String.valueOf(date.getYear()));
    }

    public void setCalendarWeekLiveData(LiveData<LocalDate> selectedYearLiveData) {

        if (lifecycleOwner == null)
            throw new IllegalStateException("Set LifeCycleOwner first!");

        selectedYearLiveData.observe(lifecycleOwner, this::updateTexts);
    }

    public void registerPreviousWeekButtonAction(Runnable runnable) {
        toPreviousWeekButton.setOnClickListener(view -> runnable.run());
    }

    public void registerNextWeekButtonAction(Runnable runnable) {
        toNextWeekButton.setOnClickListener(view -> runnable.run());
    }
}