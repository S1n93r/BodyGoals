package com.slinger.bodygoals.ui.exercises;

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
import com.slinger.bodygoals.databinding.FragmentAddGoalBinding;
import com.slinger.bodygoals.model.MuscleGroup;
import com.slinger.bodygoals.ui.ViewModel;
import com.slinger.bodygoals.ui.dtos.GoalDto;
import com.slinger.bodygoals.ui.exceptions.NoFrequencyException;

public class NewExercise extends Fragment {

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

        binding.buttonSave.setOnClickListener(addGoalView -> saveExercise(collectExerciseFromUI()));
        binding.buttonCancel.setOnClickListener(addGoalView -> backToOverview());
    }

    private void saveExercise(ExerciseDto exerciseDto) {
        /* TODO: Implement saving ExerciseDto to database. */
    }

    private void backToOverview() {
        NavHostFragment.findNavController(NewExercise.this)
                .navigate(R.id.action_new_exercise_fragment_to_OverviewFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private ExerciseDto collectExerciseFromUI() throws NoFrequencyException {
        /* TODO: Implement fetching data for ExerciseDto from view. */
        return ExerciseDto.of(null, null, null, null, 10, null);
    }

    private void registerLiveDataObserver() {

        viewModel.getSelectedGoal().observe(this, this::update);

        viewModel.getGoalEditMode().observe(this, editModeEnabled -> {

            if (editModeEnabled)
                binding.buttonSave.setText(R.string.save);
            else
                binding.buttonSave.setText(R.string.add);
        });
    }

    private void update(GoalDto goalDto) {

        binding.goalNameText.setText(goalDto.getName());
        binding.frequencyTextView.setText(String.valueOf(goalDto.getFrequency()));
        binding.selectedDateText.setText(String.valueOf(goalDto.getCreationWeek()));

        for (MuscleGroup muscleGroup : goalDto.getMuscleGroupsCopy()) {

            if (muscleGroup == MuscleGroup.ABS)
                binding.cbAbs.setChecked(true);

            if (muscleGroup == MuscleGroup.BICEPS)
                binding.cbBiceps.setChecked(true);

            if (muscleGroup == MuscleGroup.CALVES)
                binding.cbCalves.setChecked(true);

            if (muscleGroup == MuscleGroup.CHEST)
                binding.cbChest.setChecked(true);

            if (muscleGroup == MuscleGroup.FOREARMS)
                binding.cbForearms.setChecked(true);

            if (muscleGroup == MuscleGroup.HARM_STRINGS)
                binding.cbHarmstring.setChecked(true);

            if (muscleGroup == MuscleGroup.LATS)
                binding.cbLats.setChecked(true);

            if (muscleGroup == MuscleGroup.LOWER_BACK)
                binding.cbLowerBack.setChecked(true);

            if (muscleGroup == MuscleGroup.NECK)
                binding.cbNeck.setChecked(true);

            if (muscleGroup == MuscleGroup.QUADS)
                binding.cbQuads.setChecked(true);

            if (muscleGroup == MuscleGroup.SHOULDERS)
                binding.cbShoulders.setChecked(true);

            if (muscleGroup == MuscleGroup.TRICEPS)
                binding.cbTriceps.setChecked(true);
        }
    }
}