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
import com.slinger.bodygoals.databinding.FragmentYearlySummaryBinding;
import com.slinger.bodygoals.ui.ViewModel;
import com.slinger.bodygoals.ui.components.SwitchYearComponent;
import com.slinger.bodygoals.ui.dtos.YearlySummaryDto;

import java.util.Calendar;

public class YearlySummary extends Fragment {

    private ViewModel viewModel;

    private FragmentYearlySummaryBinding binding;

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
            Bundle savedInstanceState) {

        binding = FragmentYearlySummaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToOverview.setOnClickListener(addGoalView ->
                NavHostFragment.findNavController(YearlySummary.this).navigate(R.id.action_yearly_summary_fragment_to_OverviewFragment));

        SwitchYearComponent switchYearComponent = binding.switchYearComponent;

        switchYearComponent.setLifecycleOwner(this);
        switchYearComponent.setCalendarWeekLiveData(viewModel.getSelectedDate());

        switchYearComponent.registerPreviousWeekButtonAction(() -> viewModel.selectPreviousYear());
        switchYearComponent.registerNextWeekButtonAction(() -> viewModel.selectNextYear());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void registerLiveDataObserver() {
        viewModel.getYearlySummaryDtoMutableLiveData().observe(this, this::update);
    }

    private void update(YearlySummaryDto yearlySummaryDto) {

        binding.progressJan.setText(String.valueOf(yearlySummaryDto.getMonthlyProgress(Calendar.JANUARY).getProgress()));
        binding.progressFeb.setText(String.valueOf(yearlySummaryDto.getMonthlyProgress(Calendar.FEBRUARY).getProgress()));
        binding.progressMar.setText(String.valueOf(yearlySummaryDto.getMonthlyProgress(Calendar.MARCH).getProgress()));

        binding.progressApr.setText(String.valueOf(yearlySummaryDto.getMonthlyProgress(Calendar.APRIL).getProgress()));
        binding.progressMay.setText(String.valueOf(yearlySummaryDto.getMonthlyProgress(Calendar.MAY).getProgress()));
        binding.progressJun.setText(String.valueOf(yearlySummaryDto.getMonthlyProgress(Calendar.JUNE).getProgress()));

        binding.progressJul.setText(String.valueOf(yearlySummaryDto.getMonthlyProgress(Calendar.JULY).getProgress()));
        binding.progressAug.setText(String.valueOf(yearlySummaryDto.getMonthlyProgress(Calendar.AUGUST).getProgress()));
        binding.progressSep.setText(String.valueOf(yearlySummaryDto.getMonthlyProgress(Calendar.SEPTEMBER).getProgress()));

        binding.progressOct.setText(String.valueOf(yearlySummaryDto.getMonthlyProgress(Calendar.OCTOBER).getProgress()));
        binding.progressNov.setText(String.valueOf(yearlySummaryDto.getMonthlyProgress(Calendar.NOVEMBER).getProgress()));
        binding.progressDec.setText(String.valueOf(yearlySummaryDto.getMonthlyProgress(Calendar.DECEMBER).getProgress()));
    }
}