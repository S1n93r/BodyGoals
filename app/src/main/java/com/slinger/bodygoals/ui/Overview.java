package com.slinger.bodygoals.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.databinding.FragmentOverviewBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

public class Overview extends Fragment {

    private OverviewViewModel viewModel;

    private FragmentOverviewBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(OverviewViewModel.class);

        registerLiveDataObserver();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToWeeklyLog.setOnClickListener(weeklyLogView -> NavHostFragment.findNavController(Overview.this)
                .navigate(R.id.action_OverviewFragment_to_WeeklyLogFragment));

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

            String calendarWeekString = String.format(getResources().getString(R.string.calendar_week_place_holder),
                    calendarWeek.getWeek());

            binding.calendarWeekTextView.setText(calendarWeekString);

            String calendarWeekYearString = String.format(getResources().getString(R.string.calendar_week_year_place_holder),
                    calendarWeek.getYear());

            binding.calendarWeekYearTextView.setText(calendarWeekYearString);
        });
    }
}