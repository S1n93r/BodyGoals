package com.slinger.bodygoals.ui.fragments.exercises;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.databinding.FragmentNewExerciseBinding;
import com.slinger.bodygoals.model.MuscleGroup;
import com.slinger.bodygoals.model.exercises.ExerciseIdentifier;
import com.slinger.bodygoals.model.exercises.ExerciseType;
import com.slinger.bodygoals.model.exercises.ExerciseUnit;
import com.slinger.bodygoals.ui.exceptions.NoFrequencyException;
import com.slinger.bodygoals.viewmodel.ViewModel;

import java.util.Arrays;
import java.util.List;

import java8.util.Lists;
import java8.util.stream.StreamSupport;

public class NewExercise extends Fragment {

    private ViewModel viewModel;

    private FragmentNewExerciseBinding binding;

    private Spinner typeSpinner;

    private Spinner unitSpinner;

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

        binding = FragmentNewExerciseBinding.inflate(inflater, container, false);

        typeSpinner = binding.typeSpinner;
        unitSpinner = binding.unitSpinner;

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSave.setOnClickListener(addGoalView -> saveExercise(collectExerciseFromUI()));
        binding.buttonCancel.setOnClickListener(addGoalView -> backToExercises());

        typeSpinner.setOnItemSelectedListener(new TypeSelectedListener());
        unitSpinner.setOnItemSelectedListener(new UnitSelectedListener());

        setUpTypeSpinner();
        setUpUnitSpinner();
    }

    private void setUpTypeSpinner() {

        assert getActivity() != null;

        List<String> typesAsStrings = StreamSupport.stream(Arrays.asList(ExerciseType.values())).map(ExerciseType::getName).toList();

        SpinnerAdapter typeSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.component_spinner_item, typesAsStrings);

        typeSpinner.setAdapter(typeSpinnerAdapter);
    }

    private void setUpUnitSpinner() {

        assert getActivity() != null;

        List<String> unitsAsStrings = StreamSupport.stream(Arrays.asList(ExerciseUnit.values())).map(ExerciseUnit::getName).toList();

        SpinnerAdapter typeSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.component_spinner_item, unitsAsStrings);

        unitSpinner.setAdapter(typeSpinnerAdapter);
    }

    private void saveExercise(ExerciseDto exerciseDto) {

        if (exerciseDto == null)
            return;

        viewModel.addExerciseToCurrentUser(exerciseDto);

        backToExercises();
    }

    private boolean checkFormFilledAndToast() {

        if (binding.variantValue.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.toast_new_exercise_no_variant, Toast.LENGTH_SHORT).show();
            return false;
        }

        ExerciseUnit unit = ExerciseUnit.fromName((String) binding.unitSpinner.getSelectedItem());

        if (unit == ExerciseUnit.KG && binding.repGoalValue.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.toast_new_exercise_no_rep_goal, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (unit == ExerciseUnit.REPS && binding.kgGoalValue.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.toast_new_exercise_no_rep_goal, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void backToExercises() {
        NavHostFragment.findNavController(NewExercise.this)
                .navigate(R.id.action_new_exercise_fragment_to_exercises_fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private ExerciseDto collectExerciseFromUI() throws NoFrequencyException {

        if (!checkFormFilledAndToast())
            return null;

        ExerciseType exerciseType = ExerciseType.fromName((String) binding.typeSpinner.getSelectedItem());
        ExerciseUnit unit = ExerciseUnit.fromName((String) binding.unitSpinner.getSelectedItem());

        String variant = binding.variantValue.getText().toString();

        String goalString = unit == ExerciseUnit.KG
                ? binding.repGoalValue.getText().toString()
                : binding.kgGoalValue.getText().toString();

        int repGoal = Integer.parseInt(goalString);

        /* Trend 0 is okay here, because saving it via view model will update the dto via the model class. */
        return ExerciseDto.of(ExerciseIdentifier.of(exerciseType, variant), exerciseType, variant, unit, repGoal, Lists.of(), 0);
    }

    private class TypeSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            StringBuilder muscleGroupsString = new StringBuilder();

            List<MuscleGroup> muscleGroups = ExerciseType.values()[position].getMuscleGroupsStream().toList();

            for (MuscleGroup muscleGroup : muscleGroups) {

                muscleGroupsString.append(muscleGroup.getName());

                if (muscleGroups.indexOf(muscleGroup) < muscleGroups.size() - 1)
                    muscleGroupsString.append(", ");
            }

            binding.musclesValue.setText(muscleGroupsString);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            binding.musclesValue.setText("");
        }
    }

    private class UnitSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            List<ExerciseUnit> exerciseUnits = Arrays.asList(ExerciseUnit.values());

            ExerciseUnit exerciseUnit = exerciseUnits.get(position);

            switch (exerciseUnit) {

                case KG:
                    binding.repGoalValue.setVisibility(View.VISIBLE);
                    binding.repGoalLabel.setVisibility(View.VISIBLE);

                    binding.kgGoalValue.setVisibility(View.GONE);
                    binding.kgGoalLabel.setVisibility(View.GONE);

                    break;

                case REPS:
                    binding.kgGoalValue.setVisibility(View.VISIBLE);
                    binding.kgGoalLabel.setVisibility(View.VISIBLE);

                    binding.repGoalValue.setVisibility(View.GONE);
                    binding.repGoalLabel.setVisibility(View.GONE);

                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + exerciseUnit);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

            binding.repGoalValue.setVisibility(View.GONE);
            binding.kgGoalValue.setVisibility(View.GONE);

            binding.repGoalLabel.setVisibility(View.GONE);
            binding.kgGoalLabel.setVisibility(View.GONE);
        }
    }
}