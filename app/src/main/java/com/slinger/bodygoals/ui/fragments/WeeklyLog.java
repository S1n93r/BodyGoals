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
import com.slinger.bodygoals.ui.ViewModel;
import com.slinger.bodygoals.ui.components.LogEntry;
import com.slinger.bodygoals.ui.dtos.SessionDto;

import java.time.LocalDate;
import java.util.Comparator;
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

        /* Switch calendar week component */
        binding.switchCalendarWeekComponent.setLifecycleOwner(this);

        binding.switchCalendarWeekComponent.setCalendarWeekLiveData(viewModel.getSelectedDate());

        binding.switchCalendarWeekComponent.registerPreviousWeekButtonAction(() -> viewModel.selectPreviousWeekOfYear());
        binding.switchCalendarWeekComponent.registerNextWeekButtonAction(() -> viewModel.selectNextWeekOfYear());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void registerLiveDataObserver() {
        viewModel.getSelectedDate().observe(this, this::updateWeeklyLogList);
    }

    private void updateWeeklyLogList(LocalDate date) {

        List<SessionDto> sessionDtos = viewModel.getSessionsWeekOfYear(date);
        sessionDtos.sort(Comparator.comparing(SessionDto::getDate));

        binding.weeklySessionsList.removeAllViews();

        for (SessionDto sessionDto : sessionDtos) {

            LogEntry logEntry = new LogEntry(getContext());

            logEntry.setSession(sessionDto);
            logEntry.registerDisableGoalRunner(() -> {
                viewModel.removeLogEntry(sessionDto);
                updateWeeklyLogList(date);
            });

            binding.weeklySessionsList.addView(logEntry);
        }
    }
}