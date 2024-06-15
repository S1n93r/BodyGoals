package com.slinger.bodygoals.ui.fragments.exercises;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.databinding.FragmentExercisesBinding;
import com.slinger.bodygoals.ui.dtos.UserDto;
import com.slinger.bodygoals.viewmodel.ViewModel;

public class Exercises extends Fragment {

    private ViewModel viewModel;

    private FragmentExercisesBinding binding;

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

        binding = FragmentExercisesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonNewExercise.setOnClickListener(exercisesView -> toNewExercise());
        binding.buttonBack.setOnClickListener(exercisesView -> backToOverview());

        /* TODO: Move to ExerciseDetails as soon as view was build. */
        binding.buttonAddExerciseProgress.setOnClickListener(exercisesView -> toAddExerciseProgress());
    }

    private void backToOverview() {
        NavHostFragment.findNavController(Exercises.this)
                .navigate(R.id.action_exercises_fragment_to_OverviewFragment);
    }

    private void toNewExercise() {
        NavHostFragment.findNavController(Exercises.this)
                .navigate(R.id.action_exercises_fragment_to_new_exercise_fragment);
    }

    private void toAddExerciseProgress() {
        NavHostFragment.findNavController(Exercises.this)
                .navigate(R.id.action_exercises_fragment_to_add_exercise_progress_fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void registerLiveDataObserver() {
        viewModel.getCurrentUser().observe(this, this::update);
    }

    private void update(UserDto userDto) {

        LinearLayout exerciseList = binding.exerciseList;

        exerciseList.removeAllViews();

        for (ExerciseDto exerciseDto : userDto.getExerciseDtos()) {

            ExerciseEntry exerciseEntry = new ExerciseEntry(getContext());
            exerciseEntry.setExerciseDto(exerciseDto);

            exerciseList.addView(exerciseEntry);
        }
    }
}