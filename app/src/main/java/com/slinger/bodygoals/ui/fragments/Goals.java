package com.slinger.bodygoals.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.databinding.FragmentGoalsBinding;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.ui.ViewModel;
import com.slinger.bodygoals.ui.components.GoalEntry;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

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
                    NavHostFragment.findNavController(Goals.this).navigate(R.id.action_goals_list_fragment_to_AddGoalFragment);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void registerLiveDataObserver() {
        viewModel.getUserGoals().observe(this, this::updateGoalsList);
    }

    private void updateGoalsList(List<Goal> goals) {

        binding.goalsList.removeAllViews();

        for (Goal goal : goals) {

            GoalEntry goalEntry = new GoalEntry(getContext());

            goalEntry.setGoal(goal);
            goalEntry.registerEnableGoalRunner(() -> viewModel.enableGoal(goal));
            goalEntry.registerDisableGoalRunner(() -> viewModel.disableGoal(goal));
            goalEntry.registerDeleteGoalRunner(() -> viewModel.deleteGoal(goal));

            binding.goalsList.addView(goalEntry);
        }
    }
}