package com.slinger.bodygoals.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.databinding.FragmentAddGoalBinding;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.MuscleGroup;
import com.slinger.bodygoals.ui.ViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

public class AddGoal extends Fragment {

    private ViewModel viewModel;

    private FragmentAddGoalBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null)
            viewModel = new ViewModelProvider(getActivity()).get(ViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddGoalBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToOverview.setOnClickListener(addGoalView -> navigateToOverview());

        binding.buttonSave.setOnClickListener(addGoalView -> {
            viewModel.addGoal(collectGoalFromUI());
            navigateToOverview();
        });
    }

    private void navigateToOverview() {
        NavHostFragment.findNavController(AddGoal.this).navigate(R.id.action_AddGoalFragment_to_goals_list_fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private Goal collectGoalFromUI() {

        String frequencyString = binding.frequencyTextView.getText().toString();

        /* TODO: Turn into user feedback */
        if (frequencyString.isEmpty())
            throw new IllegalStateException("Frequency can't be empty.");

        int frequency = Integer.parseInt(frequencyString);

        String goalName = binding.goalNameText.getText().toString();

        /* TODO: Turn into user feedback */
        if (goalName.isEmpty())
            throw new IllegalStateException("Goal name can't be empty.");

        boolean absChecked = binding.cbAbs.isChecked();
        boolean bicepsChecked = binding.cbBiceps.isChecked();
        boolean calvesChecked = binding.cbCalves.isChecked();
        boolean chestChecked = binding.cbChest.isChecked();
        boolean forearmsChecked = binding.cbForearms.isChecked();
        boolean harmStringsChecked = binding.cbHarmstring.isChecked();
        boolean latsChecked = binding.cbLats.isChecked();
        boolean quadsChecked = binding.cbQuads.isChecked();
        boolean shouldersChecked = binding.cbShoulders.isChecked();
        boolean tricepsChecked = binding.cbTriceps.isChecked();

        Goal goal = Goal.of(goalName, frequency);

        if (absChecked)
            goal.addMuscleGroup(new MuscleGroup("Abs"));

        if (bicepsChecked)
            goal.addMuscleGroup(new MuscleGroup("Biceps"));

        if (calvesChecked)
            goal.addMuscleGroup(new MuscleGroup("Calves"));

        if (chestChecked)
            goal.addMuscleGroup(new MuscleGroup("Chest"));

        if (forearmsChecked)
            goal.addMuscleGroup(new MuscleGroup("Forearms"));

        if (harmStringsChecked)
            goal.addMuscleGroup(new MuscleGroup("Harm Strings"));

        if (latsChecked)
            goal.addMuscleGroup(new MuscleGroup("Lats"));

        if (quadsChecked)
            goal.addMuscleGroup(new MuscleGroup("Quads"));

        if (shouldersChecked)
            goal.addMuscleGroup(new MuscleGroup("Shoulders"));

        if (tricepsChecked)
            goal.addMuscleGroup(new MuscleGroup("Abs"));

        return goal;
    }
}