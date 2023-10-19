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
import com.slinger.bodygoals.databinding.FragmentOverviewBinding;
import com.slinger.bodygoals.model.CalendarWeek;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.ui.ViewModel;
import com.slinger.bodygoals.ui.components.OverviewEntryComponent;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        binding.buttonAddSession.setOnClickListener(overviewView -> NavHostFragment.findNavController(Overview.this)
                .navigate(R.id.action_OverviewFragment_to_AddSessionFragment));

        binding.buttonToCoverage.setOnClickListener(overviewView -> NavHostFragment.findNavController(Overview.this)
                .navigate(R.id.action_OverviewFragment_to_CoverageFragment));

        /* Switch calendar week component */
        binding.switchCalendarWeekComponent.setLifecycleOwner(this);

        binding.switchCalendarWeekComponent.setCalendarWeekLiveData(viewModel.getSelectedCalendarWeek());

        binding.switchCalendarWeekComponent.registerPreviousWeekButtonAction(() -> viewModel.selectPreviousWeek());
        binding.switchCalendarWeekComponent.registerNextWeekButtonAction(() -> viewModel.selectNextWeek());
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

        viewModel.getSelectedCalendarWeek().observe(this, this::updateGoalProgressBars);
    }

    private void updateGoalProgressBars(CalendarWeek calendarWeek) {

        List<Goal> goalsCopy = viewModel.getUserGoals().getValue();

        if (goalsCopy == null)
            throw new IllegalStateException("Live data for goals should never be null.");

        Set<Goal> goals = new HashSet<>(goalsCopy);

        goals.addAll(viewModel.getSessionGoals(calendarWeek));

        binding.goalProgressBarsList.removeAllViews();

        int maxOverallProgress = 0;
        int currentOverallProgress = 0;

        for (Goal goal : goals) {

            if (calendarWeek.isAfter(goal.getCreationWeek()))
                continue;

            OverviewEntryComponent overviewEntryComponent = new OverviewEntryComponent(getContext());

            int progress = viewModel.getGoalProgress(calendarWeek, goal);

            overviewEntryComponent.setProgress(progress);
            overviewEntryComponent.setText(goal.getName());

            maxOverallProgress += goal.getFrequency();
            currentOverallProgress += Math.min(goal.getFrequency(), viewModel.getSessionsLogged(calendarWeek, goal));

            binding.goalProgressBarsList.addView(overviewEntryComponent);
        }

        int overallProgress = (int) Math.round((double) currentOverallProgress / (double) maxOverallProgress * 100);
        binding.overallProgressBar.setProgress(overallProgress);
    }
}