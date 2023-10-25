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

        switchYearComponent.registerPreviousWeekButtonAction(() -> viewModel.selectPreviousDate(Calendar.YEAR));
        switchYearComponent.registerNextWeekButtonAction(() -> viewModel.selectNextDate(Calendar.YEAR));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void registerLiveDataObserver() {

    }
}