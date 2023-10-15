package com.slinger.bodygoals.ui.fragments;

import android.graphics.Color;
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
import com.slinger.bodygoals.databinding.FragmentCoverageBinding;
import com.slinger.bodygoals.model.CalendarWeek;
import com.slinger.bodygoals.model.User;
import com.slinger.bodygoals.ui.ViewModel;

public class CoverageFragment extends Fragment {

    private static final int COLOR_ID_BACKGROUND_GREEN = Color.parseColor("#408053");
    private static final int COLOR_ID_STROKE_GREEN = Color.parseColor("#80ffa6");

    private static final int COLOR_ID_BACKGROUND_YELLOW = Color.parseColor("#fffb80");
    private static final int COLOR_ID_STROKE_YELLOW = Color.parseColor("#807d40");

    private static final int COLOR_ID_BACKGROUND_RED = Color.parseColor("#ff8080");
    private static final int COLOR_ID_STROKE_RED = Color.parseColor("#804040");

    private ViewModel viewModel;

    private FragmentCoverageBinding binding;

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

        binding = FragmentCoverageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToOverview.setOnClickListener(addGoalView ->
                NavHostFragment.findNavController(CoverageFragment.this).navigate(R.id.action_CoverageFragment_to_OverviewFragment));

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

        viewModel.getCurrentUser().observe(this,
                user -> updateCoverage(user, viewModel.getSelectedCalendarWeek().getValue()));

        viewModel.getSelectedCalendarWeek().observe(this, calendarWeek ->
                updateCoverage(viewModel.getCurrentUser().getValue(), calendarWeek));
    }

    private void updateCoverage(User user, CalendarWeek calendarWeek) {
        /* TODO: For this implementation the model has to prepare testable classes. */
    }
}