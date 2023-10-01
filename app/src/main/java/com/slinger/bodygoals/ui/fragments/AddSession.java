package com.slinger.bodygoals.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.databinding.FragmentAddSessionBinding;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.Session;
import com.slinger.bodygoals.ui.ViewModel;
import com.slinger.bodygoals.ui.components.GoalCheckBox;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import java8.util.Lists;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class AddSession extends Fragment {

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

        binding.buttonToOverview.setOnClickListener(addGoalView -> navigateToOverview());

        binding.buttonSave.setOnClickListener(addGoalView -> {

            /* TODO: Grab goals from component that shows all available goals. */
            List<Goal> goals = Lists.of();

            viewModel.addSessions(collectSessionsFromUI(goals));
            navigateToOverview();
        });
    }

    private void navigateToOverview() {
        NavHostFragment.findNavController(AddSession.this).navigate(R.id.action_AddSessionFragment_to_OverviewFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<Session> collectSessionsFromUI(List<Goal> goalList) {

        /* TODO: Here we have to grab the date from a date picker component. */
        Date date = Calendar.getInstance().getTime();

        return StreamSupport.stream(goalList)
                .map(goal -> new Session(goal, date))
                .collect(Collectors.toList());
    }

    private void registerLiveDataObserver() {

        viewModel.getUserGoals().observe(this, goals -> {

            for (Goal goal : goals) {

                if (getContext() == null)
                    throw new IllegalStateException("Context is null. Check why.");

                GoalCheckBox checkBox = new GoalCheckBox(getContext());
                checkBox.setGoal(goal);

                binding.goalList.addView(checkBox);
            }
        });
    }
}