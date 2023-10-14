package com.slinger.bodygoals.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.databinding.FragmentOverviewBinding;
import com.slinger.bodygoals.model.CalendarWeek;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.ui.ViewModel;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

public class Overview extends Fragment {

    private ViewModel viewModel;

    private FragmentOverviewBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null)
            viewModel = new ViewModelProvider(getActivity()).get(ViewModel.class);

        registerLiveDataObserver();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToWeeklyLog.setOnClickListener(overviewView -> NavHostFragment.findNavController(Overview.this)
                .navigate(R.id.action_OverviewFragment_to_WeeklyLogFragment));

        binding.buttonToAddGoal.setOnClickListener(overviewView -> NavHostFragment.findNavController(Overview.this)
                .navigate(R.id.action_OverviewFragment_to_goals_list_fragment));

        binding.buttonToAddSession.setOnClickListener(overviewView -> NavHostFragment.findNavController(Overview.this)
                .navigate(R.id.action_OverviewFragment_to_AddSessionFragment));

        binding.buttonPreviousWeek.setOnClickListener(weeklyLogView -> viewModel.selectPreviousWeek());
        binding.buttonNextWeek.setOnClickListener(weeklyLogView -> viewModel.selectNextWeek());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void registerLiveDataObserver() {

        viewModel.getUserGoals().observe(this, goals -> {

            CalendarWeek selectedCalendarWeek = viewModel.getSelectedCalendarWeek().getValue();
            CalendarWeek calendarWeek = selectedCalendarWeek == null
                    ? CalendarWeek.from(Calendar.getInstance().getTime())
                    : selectedCalendarWeek;

            updateGoalProgressBars(calendarWeek);
        });

        viewModel.getSelectedCalendarWeek().observe(this, calendarWeek -> {
            updateCalendarWeekLabel(calendarWeek);
            updateGoalProgressBars(calendarWeek);
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

    /* TODO: Remove this method */
    private void updateGoalProgressBars(List<Goal> goals) {

        binding.goalProgressBarsList.removeAllViews();

        for (Goal goal : goals) {

            /* TODO: This should be solved via dedicated class. */
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View labeledProgressBar = inflater.inflate(R.layout.component_labeled_progress, null);

            TextView goalNameText = labeledProgressBar.findViewById(R.id.log_entry_text);

            goalNameText.setText(goal.getName());

            binding.goalProgressBarsList.addView(labeledProgressBar);
        }
    }

    private void updateGoalProgressBars(CalendarWeek calendarWeek) {

        List<Goal> goals = viewModel.getUserGoals().getValue();

        if (goals == null)
            return;

        binding.goalProgressBarsList.removeAllViews();

        int maxOverallProgress = 0;
        int currentOverallProgress = 0;

        /* TODO: Overview should be based on logged sessions, not goals. */
        /* Update sub-progress */
        for (Goal goal : goals) {

            /* TODO: This should be solved via dedicated class. */
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View labeledProgressBar = inflater.inflate(R.layout.component_labeled_progress, null);

            TextView goalNameText = labeledProgressBar.findViewById(R.id.log_entry_text);
            ProgressBar goalProgressBar = labeledProgressBar.findViewById(R.id.goal_progress_bar);

            int progress = viewModel.getGoalProgress(calendarWeek, goal);

            goalProgressBar.setProgress(progress);
            goalNameText.setText(goal.getName());

            maxOverallProgress += goal.getFrequency();
            currentOverallProgress += Math.min(goal.getFrequency(), viewModel.getSessionsLogged(calendarWeek, goal));

            binding.goalProgressBarsList.addView(labeledProgressBar);
        }

        int overallProgress = (int) Math.round((double) currentOverallProgress / (double) maxOverallProgress * 100);
        binding.overallProgressBar.setProgress(overallProgress);
    }
}