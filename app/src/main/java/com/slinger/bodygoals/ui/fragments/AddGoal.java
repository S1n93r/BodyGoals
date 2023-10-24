package com.slinger.bodygoals.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.databinding.FragmentAddGoalBinding;
import com.slinger.bodygoals.model.CalendarWeek;
import com.slinger.bodygoals.model.Goal;
import com.slinger.bodygoals.model.MuscleGroup;
import com.slinger.bodygoals.model.exceptions.GoalAlreadyExistsException;
import com.slinger.bodygoals.ui.ViewModel;
import com.slinger.bodygoals.ui.components.DatePickerFragment;
import com.slinger.bodygoals.ui.exceptions.NoFrequencyException;
import com.slinger.bodygoals.ui.exceptions.NoGoalNameException;
import com.slinger.bodygoals.ui.exceptions.NoMuscleGroupException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AddGoal extends Fragment {

    private final DatePickerFragment datePickerFragment = new DatePickerFragment();

    private ViewModel viewModel;

    private FragmentAddGoalBinding binding;

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

        binding = FragmentAddGoalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCancel.setOnClickListener(addGoalView -> navigateToGoalsList());

        binding.buttonSave.setOnClickListener(addGoalView -> {

            try {

                viewModel.addGoal(collectGoalFromUI());
                navigateToGoalsList();

            } catch (IllegalStateException e) {

                if (e instanceof NoFrequencyException)
                    Toast.makeText(getContext(), R.string.toast_no_frequency, Toast.LENGTH_SHORT).show();
                else if (e instanceof NoGoalNameException)
                    Toast.makeText(getContext(), R.string.toast_no_goal_name, Toast.LENGTH_SHORT).show();
                else if (e instanceof NoMuscleGroupException)
                    Toast.makeText(getContext(), R.string.toast_no_muscle_groups, Toast.LENGTH_SHORT).show();
                else if (e instanceof GoalAlreadyExistsException)
                    Toast.makeText(getContext(), R.string.goal_already_exists, Toast.LENGTH_SHORT).show();
            }
        });

        binding.selectedDateText.setOnClickListener(addSessionView -> {
                    if (!datePickerFragment.isAdded())
                        datePickerFragment.show(getParentFragmentManager(), "timePicker");
                }
        );
    }

    private void navigateToGoalsList() {
        NavHostFragment.findNavController(AddGoal.this)
                .navigate(R.id.action_AddGoalFragment_to_goals_list_fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private Goal collectGoalFromUI() throws NoFrequencyException {

        String frequencyString = binding.frequencyTextView.getText().toString();

        String goalName = binding.goalNameText.getText().toString();

        if (goalName.isEmpty())
            throw new NoGoalNameException();

        if (frequencyString.isEmpty())
            throw new NoFrequencyException();

        int frequency = Integer.parseInt(frequencyString);

        boolean absChecked = binding.cbAbs.isChecked();
        boolean bicepsChecked = binding.cbBiceps.isChecked();
        boolean calvesChecked = binding.cbCalves.isChecked();
        boolean chestChecked = binding.cbChest.isChecked();
        boolean forearmsChecked = binding.cbForearms.isChecked();
        boolean harmStringsChecked = binding.cbHarmstring.isChecked();
        boolean latsChecked = binding.cbLats.isChecked();
        boolean lowerBackChecked = binding.cbLowerBack.isChecked();
        boolean neckChecked = binding.cbNeck.isChecked();
        boolean quadsChecked = binding.cbQuads.isChecked();
        boolean shouldersChecked = binding.cbShoulders.isChecked();
        boolean tricepsChecked = binding.cbTriceps.isChecked();

        CalendarWeek calendarWeek = CalendarWeek.from(datePickerFragment.getSelectedDateLiveData().getValue());

        Goal goal = Goal.of(goalName, frequency, calendarWeek);

        List<MuscleGroup> selectedMuscleGroups = new ArrayList<>();

        if (lowerBackChecked)
            selectedMuscleGroups.add(MuscleGroup.LOWER_BACK);

        if (neckChecked)
            selectedMuscleGroups.add(MuscleGroup.NECK);

        if (absChecked)
            selectedMuscleGroups.add(MuscleGroup.ABS);

        if (chestChecked)
            selectedMuscleGroups.add(MuscleGroup.CHEST);

        if (latsChecked)
            selectedMuscleGroups.add(MuscleGroup.LATS);

        if (shouldersChecked)
            selectedMuscleGroups.add(MuscleGroup.SHOULDERS);

        if (bicepsChecked)
            selectedMuscleGroups.add(MuscleGroup.BICEPS);

        if (tricepsChecked)
            selectedMuscleGroups.add(MuscleGroup.TRICEPS);

        if (forearmsChecked)
            selectedMuscleGroups.add(MuscleGroup.FOREARMS);

        if (quadsChecked)
            selectedMuscleGroups.add(MuscleGroup.QUADS);

        if (harmStringsChecked)
            selectedMuscleGroups.add(MuscleGroup.HARM_STRINGS);

        if (calvesChecked)
            selectedMuscleGroups.add(MuscleGroup.CALVES);

        if (selectedMuscleGroups.isEmpty())
            throw new NoMuscleGroupException();

        selectedMuscleGroups.forEach(goal::addMuscleGroup);

        return goal;
    }

    private void registerLiveDataObserver() {

        datePickerFragment.getSelectedDateLiveData().observe(this, date -> {

            String weekString = new SimpleDateFormat("w").format(date);

            binding.selectedDateText.setText(weekString);
        });
    }
}