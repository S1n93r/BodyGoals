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
import com.slinger.bodygoals.databinding.FragmentGoalsBinding;
import com.slinger.bodygoals.ui.ViewModel;
import com.slinger.bodygoals.ui.components.GoalEntry;
import com.slinger.bodygoals.ui.dtos.GoalDto;

import java.util.List;

public class Goals extends Fragment {

    private ViewModel viewModel;

    private FragmentGoalsBinding binding;

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

        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToOverview.setOnClickListener(addGoalView ->
                NavHostFragment.findNavController(Goals.this).navigate(R.id.action_goals_list_fragment_to_OverviewFragment));

        binding.buttonAdd.setOnClickListener(addGoalView -> {
            viewModel.clearSelectGoal();
            NavHostFragment.findNavController(Goals.this).navigate(R.id.action_goals_list_fragment_to_AddGoalFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void registerLiveDataObserver() {
        viewModel.getCurrentUser().observe(this, userDto -> updateGoalsList(userDto.getGoalDtos()));
    }

    private void updateGoalsList(List<GoalDto> goalDtos) {

        binding.goalsList.removeAllViews();

        for (GoalDto goalDto : goalDtos) {

            GoalEntry goalEntry = new GoalEntry(getContext());

            goalEntry.setGoalDto(goalDto);

            goalEntry.registerEditGoalRunner(() -> {

                viewModel.selectGoal(goalDto);
                viewModel.enableGoalEditMode();

                NavHostFragment.findNavController(Goals.this).navigate(R.id.action_goals_list_fragment_to_AddGoalFragment);
            });

            goalEntry.registerDeleteGoalRunner(() -> viewModel.deleteGoal(goalDto));

            binding.goalsList.addView(goalEntry);
        }
    }
}