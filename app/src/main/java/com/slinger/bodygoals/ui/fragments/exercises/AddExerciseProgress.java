package com.slinger.bodygoals.ui.fragments.exercises;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.databinding.FragmentAddExerciseProgressBinding;
import com.slinger.bodygoals.model.exercises.ExerciseIdentifier;
import com.slinger.bodygoals.ui.dtos.UserDto;
import com.slinger.bodygoals.viewmodel.ViewModel;

import java.util.List;

import java8.util.stream.StreamSupport;

public class AddExerciseProgress extends Fragment {

    private ViewModel viewModel;

    private FragmentAddExerciseProgressBinding binding;

    private Spinner exerciseIdSpinner;

    private EditText maxEffortTextEdit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null)
            viewModel = new ViewModelProvider(getActivity()).get(ViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentAddExerciseProgressBinding.inflate(inflater, container, false);

        exerciseIdSpinner = binding.exerciseIdSpinner;
        maxEffortTextEdit = binding.maxEffortValue;

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSave.setOnClickListener(addGoalView -> saveExercise(collectExerciseIdFromUI(), collectMaxEffortFromUI()));
        binding.buttonCancel.setOnClickListener(addGoalView -> backToExercises());

        setUpExerciseIdSpinner();
    }

    private ExerciseIdentifier collectExerciseIdFromUI() {
        return (ExerciseIdentifier) exerciseIdSpinner.getSelectedItem();
    }

    private double collectMaxEffortFromUI() {

        if (maxEffortTextEdit.getText().toString().isEmpty())
            Toast.makeText(getContext(), R.string.toast_add_exercise_progress_no_max_effort, Toast.LENGTH_SHORT).show();

        return Integer.parseInt(maxEffortTextEdit.getText().toString());
    }

    private void setUpExerciseIdSpinner() {

        assert getActivity() != null;

        UserDto userDto = viewModel.getCurrentUser().getValue();

        assert userDto != null;

        List<ExerciseIdentifier> exerciseIds = StreamSupport.stream(userDto.getExerciseDtos()).map(ExerciseDto::getExerciseIdentifier).toList();

        SpinnerAdapter typeSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.component_spinner_item, exerciseIds);

        exerciseIdSpinner.setAdapter(typeSpinnerAdapter);
    }

    private void saveExercise(ExerciseIdentifier exerciseIdentifier, double maxEffort) {

        viewModel.addExerciseProgress(exerciseIdentifier, maxEffort);

        backToExercises();
    }

    private void backToExercises() {
        NavHostFragment.findNavController(AddExerciseProgress.this)
                .navigate(R.id.action_add_exercise_progress_fragment_to_exercises_fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}