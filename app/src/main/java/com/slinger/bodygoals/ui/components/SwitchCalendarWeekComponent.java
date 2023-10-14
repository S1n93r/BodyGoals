package com.slinger.bodygoals.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.model.CalendarWeek;

public class SwitchCalendarWeekComponent extends ConstraintLayout {

    private final LifecycleOwner lifecycleOwner;

    private TextView weekText;
    private TextView yearText;

    private ImageView toPreviousWeekButton;
    private ImageView toNextWeekButton;

    public SwitchCalendarWeekComponent(Context context, LifecycleOwner lifecycleOwner) {

        super(context);

        this.lifecycleOwner = lifecycleOwner;

        configureComponents(context);
    }

    public SwitchCalendarWeekComponent(Context context, @Nullable AttributeSet attrs, LifecycleOwner lifecycleOwner) {

        super(context, attrs);

        this.lifecycleOwner = lifecycleOwner;

        configureComponents(context);
    }

    public SwitchCalendarWeekComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, LifecycleOwner lifecycleOwner) {

        super(context, attrs, defStyleAttr);

        this.lifecycleOwner = lifecycleOwner;

        configureComponents(context);
    }

    public SwitchCalendarWeekComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes, LifecycleOwner lifecycleOwner) {

        super(context, attrs, defStyleAttr, defStyleRes);

        this.lifecycleOwner = lifecycleOwner;

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

    private void updateTexts(CalendarWeek calendarWeek) {

        String calendarWeekString = String.format(getResources().getString(R.string.calendar_week_place_holder),
                calendarWeek.getWeek());

        weekText.setText(calendarWeekString);

        String calendarWeekYearString = String.format(getResources().getString(R.string.calendar_week_year_place_holder),
                calendarWeek.getYear());

        yearText.setText(calendarWeekYearString);
    }

    private void setCalendarWeekLiveData(LiveData<CalendarWeek> calendarWeekLiveData) {
        calendarWeekLiveData.observe(lifecycleOwner, this::updateTexts);
    }

    public void registerPreviousWeekButtonAction(Runnable runnable) {
        toPreviousWeekButton.setOnClickListener(view -> runnable.run());
    }

    public void registerNextWeekButtonAction(Runnable runnable) {
        toNextWeekButton.setOnClickListener(view -> runnable.run());
    }
}