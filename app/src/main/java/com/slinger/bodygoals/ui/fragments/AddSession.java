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
import com.slinger.bodygoals.databinding.FragmentAddSessionBinding;
import com.slinger.bodygoals.model.log.SessionIdentifier;
import com.slinger.bodygoals.ui.ViewModel;
import com.slinger.bodygoals.ui.components.DatePickerFragment;
import com.slinger.bodygoals.ui.components.GoalCheckBox;
import com.slinger.bodygoals.ui.dtos.GoalDto;
import com.slinger.bodygoals.ui.dtos.SessionDto;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class AddSession extends Fragment {

    private final DatePickerFragment datePickerFragment = new DatePickerFragment();

    private ViewModel viewModel;

    private FragmentAddSessionBinding binding;

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

        binding = FragmentAddSessionBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        binding.buttonToOverview.setOnClickListener(addSessionView ->
                NavHostFragment.findNavController(AddSession.this).navigate(
                        R.id.action_AddSessionFragment_to_OverviewFragment));

        binding.buttonStart.setOnClickListener(addSessionView -> {

            viewModel.setPreSavedSessions(collectSessionsFromUI());

            NavHostFragment.findNavController(AddSession.this).navigate(
                    R.id.action_AddSessionFragment_to_run_session_fragment);
        });

        binding.selectedDateText.setOnClickListener(addSessionView -> {
                    if (!datePickerFragment.isAdded())
                        datePickerFragment.show(getParentFragmentManager(), "timePicker");
                }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<SessionDto> collectSessionsFromUI() {

        List<GoalDto> goalDtos = new ArrayList<>(binding.goalList.getChildCount());

        for (int i = 0; i < binding.goalList.getChildCount(); i++) {

            GoalCheckBox goalCheckBox = (GoalCheckBox) binding.goalList.getChildAt(i);

            if (goalCheckBox.isChecked())
                goalDtos.add(goalCheckBox.getGoal());
        }

        LocalDate date = datePickerFragment.getSelectedDateLiveData().getValue();

        return StreamSupport.stream(goalDtos)
                .map(goal -> SessionDto.of(SessionIdentifier.DEFAULT, goal, date))
                .collect(Collectors.toList());
    }

    private void registerLiveDataObserver() {

        viewModel.getCurrentUser().observe(this, userDto -> {

            binding.goalList.removeAllViews();

            for (GoalDto goal : userDto.getGoalDtos()) {

                if (getContext() == null)
                    throw new IllegalStateException("Context is null. Check why.");

                GoalCheckBox checkBox = new GoalCheckBox(getContext());
                checkBox.setGoal(goal);

                binding.goalList.addView(checkBox);
            }
        });

        datePickerFragment.getSelectedDateLiveData().observe(this, date -> {

            DateFormat dateFormat = DateFormat.getInstance();

            binding.selectedDateText.setText(dateFormat.format(date));
        });
    }
}