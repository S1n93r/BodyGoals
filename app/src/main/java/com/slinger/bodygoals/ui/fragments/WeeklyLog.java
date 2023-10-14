package com.slinger.bodygoals.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.databinding.FragmentWeeklyLogBinding;
import com.slinger.bodygoals.model.CalendarWeek;
import com.slinger.bodygoals.model.Session;
import com.slinger.bodygoals.ui.ViewModel;
import com.slinger.bodygoals.ui.components.LogEntryComponent;

import java.util.List;

public class WeeklyLog extends Fragment {

    private ViewModel viewModel;

    private FragmentWeeklyLogBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null)
            viewModel = new ViewModelProvider(getActivity()).get(ViewModel.class);

        registerLiveDataObserver();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentWeeklyLogBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToOverview.setOnClickListener(overviewView -> NavHostFragment.findNavController(WeeklyLog.this)
                .navigate(R.id.action_WeeklyLogFragment_to_OverviewFragment));

        binding.buttonPreviousWeek.setOnClickListener(weeklyLogView -> viewModel.selectPreviousWeek());
        binding.buttonNextWeek.setOnClickListener(weeklyLogView -> viewModel.selectNextWeek());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void registerLiveDataObserver() {
        viewModel.getSelectedCalendarWeek().observe(this, calendarWeek -> {
            updateCalendarWeekLabel(calendarWeek);
            updateWeeklyLogList(calendarWeek);
        });
    }

    /* TODO: Component and logic for calendar week switching exists twice. Build custom component. */
    private void updateCalendarWeekLabel(CalendarWeek calendarWeek) {

        String calendarWeekString = String.format(getResources().getString(R.string.calendar_week_place_holder),
                calendarWeek.getWeek());

        binding.calendarWeekText.setText(calendarWeekString);

        String calendarWeekYearString = String.format(getResources().getString(R.string.calendar_week_year_place_holder),
                calendarWeek.getYear());

        binding.calendarWeekYearText.setText(calendarWeekYearString);
    }

    private void updateWeeklyLogList(CalendarWeek calendarWeek) {

        List<Session> sessions = viewModel.getSessions(calendarWeek);

        binding.weeklySessionsList.removeAllViews();

        for (Session session : sessions) {

            LogEntryComponent logEntryComponent = new LogEntryComponent(getContext());

            logEntryComponent.setSession(session);
            logEntryComponent.registerDisableGoalRunner(() -> {
                viewModel.removeLogEntry(session);
                updateWeeklyLogList(calendarWeek);
            });

            binding.weeklySessionsList.addView(logEntryComponent);
        }
    }
}