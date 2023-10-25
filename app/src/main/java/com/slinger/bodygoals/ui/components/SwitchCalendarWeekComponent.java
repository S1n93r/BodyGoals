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
import com.slinger.bodygoals.model.DateUtil;

import java.util.Calendar;
import java.util.Date;

public class SwitchCalendarWeekComponent extends ConstraintLayout {

    private LifecycleOwner lifecycleOwner;

    private TextView weekText;
    private TextView yearText;

    private Button toPreviousWeekButton;
    private Button toNextWeekButton;

    public SwitchCalendarWeekComponent(Context context) {

        super(context);

        configureComponents(context);
    }

    public SwitchCalendarWeekComponent(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        configureComponents(context);
    }

    public SwitchCalendarWeekComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        configureComponents(context);
    }

    public SwitchCalendarWeekComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        configureComponents(context);
    }

    private void configureComponents(Context context) {

        View innerView = inflate(context, R.layout.component_switch_calendar_week, null);

        weekText = innerView.findViewById(R.id.calendar_week_Text);
        yearText = innerView.findViewById(R.id.calendar_week_year_text);

        toPreviousWeekButton = innerView.findViewById(R.id.button_previous_week);
        toNextWeekButton = innerView.findViewById(R.id.button_next_week);

        this.addView(innerView);
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    private void updateTexts(Date date) {

        int weekOfYear = DateUtil.getFromDate(date, Calendar.WEEK_OF_YEAR);

        String calendarWeekString = String.format(getResources().getString(R.string.calendar_week_place_holder),
                weekOfYear);

        weekText.setText(calendarWeekString);

        String calendarWeekYearString = String.format(getResources().getString(R.string.calendar_week_year_place_holder),
                weekOfYear);

        yearText.setText(calendarWeekYearString);
    }

    public void setCalendarWeekLiveData(LiveData<Date> calendarWeekLiveData) {

        if (lifecycleOwner == null)
            throw new IllegalStateException("Set LifeCycleOwner first!");

        calendarWeekLiveData.observe(lifecycleOwner, this::updateTexts);
    }

    public void registerPreviousWeekButtonAction(Runnable runnable) {
        toPreviousWeekButton.setOnClickListener(view -> runnable.run());
    }

    public void registerNextWeekButtonAction(Runnable runnable) {
        toNextWeekButton.setOnClickListener(view -> runnable.run());
    }
}