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
import com.slinger.bodygoals.databinding.FragmentNewExerciseBinding;
import com.slinger.bodygoals.model.MuscleGroup;
import com.slinger.bodygoals.ui.ViewModel;
import com.slinger.bodygoals.ui.exceptions.NoFrequencyException;

public class NewExercise extends Fragment {

    private ViewModel viewModel;

    private FragmentNewExerciseBinding binding;

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

        binding = FragmentNewExerciseBinding.inflate(inflater, container, false);
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
                .navigate(R.id.action_new_exercise_fragment_to_exercises_fragment);
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

        viewModel.getSelectedExercise().observe(this, this::update);

        viewModel.getGoalEditMode().observe(this, editModeEnabled -> {

            if (editModeEnabled)
                binding.buttonSave.setText(R.string.save);
            else
                binding.buttonSave.setText(R.string.add);
        });
    }

    private void update(ExerciseDto exerciseDto) {

        StringBuilder musclesStringBuilder = new StringBuilder();

        exerciseDto.getExerciseIdentifier().getExerciseType().getMuscleGroupsStream()
                .map(MuscleGroup::getName)
                .forEach(musclesStringBuilder::append);

        binding.musclesValue.setText(musclesStringBuilder.toString());
    }
}