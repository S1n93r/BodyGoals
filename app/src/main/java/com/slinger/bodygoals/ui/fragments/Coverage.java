package com.slinger.bodygoals.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.databinding.FragmentCoverageBinding;
import com.slinger.bodygoals.model.MuscleGroup;
import com.slinger.bodygoals.model.Progress;
import com.slinger.bodygoals.model.ProgressStatus;
import com.slinger.bodygoals.model.User;
import com.slinger.bodygoals.ui.ViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Coverage extends Fragment {

    private ViewModel viewModel;

    private FragmentCoverageBinding binding;

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

        binding = FragmentCoverageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToOverview.setOnClickListener(addGoalView ->
                NavHostFragment.findNavController(Coverage.this).navigate(R.id.action_CoverageFragment_to_OverviewFragment));

        /* Switch calendar week component */
        binding.switchCalendarWeekComponent.setLifecycleOwner(this);

        binding.switchCalendarWeekComponent.setCalendarWeekLiveData(viewModel.getSelectedDate());

        binding.switchCalendarWeekComponent.registerPreviousWeekButtonAction(() -> viewModel.selectPreviousDate(Calendar.WEEK_OF_YEAR));
        binding.switchCalendarWeekComponent.registerNextWeekButtonAction(() -> viewModel.selectNextDate(Calendar.WEEK_OF_YEAR));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void registerLiveDataObserver() {

        viewModel.getCurrentUser().observe(this,
                user -> updateCoverage(user, viewModel.getSelectedDate().getValue()));

        viewModel.getSelectedDate().observe(this, selectedDate -> {

            User user = viewModel.getCurrentUser().getValue();

            /* TODO: Meh. */
            if (user == null)
                return;

            updateCoverage(user, selectedDate);
        });
    }

    private void updateCoverage(User user, Date date) {

        Map<MuscleGroup, Progress> progressPerMuscleGroup = user.getSessionLog().progressPerMuscleGroup(date);

        progressPerMuscleGroup.forEach((key, value) -> {

            ProgressStatus progressStatus = value.getProgressStatus();

            switch (key) {

                case NECK:
                    binding.buttonHead.setBackground(fromProgressStatus(progressStatus));
                    break;

                case CHEST:
                    binding.buttonChest.setBackground(fromProgressStatus(progressStatus));
                    break;

                case LATS:
                    binding.buttonLats.setBackground(fromProgressStatus(progressStatus));
                    break;

                case SHOULDERS:
                    binding.buttonShoulderLeft.setBackground(fromProgressStatus(progressStatus));
                    binding.buttonShoulderRight.setBackground(fromProgressStatus(progressStatus));
                    break;

                case BICEPS:
                    binding.buttonBicepsLeft.setBackground(fromProgressStatus(progressStatus));
                    binding.buttonBicepsRight.setBackground(fromProgressStatus(progressStatus));
                    break;

                case TRICEPS:
                    binding.buttonTricepsLeft.setBackground(fromProgressStatus(progressStatus));
                    binding.buttonTricepsRight.setBackground(fromProgressStatus(progressStatus));
                    break;

                case FOREARMS:
                    binding.buttonForearmLeft.setBackground(fromProgressStatus(progressStatus));
                    binding.buttonForearmRight.setBackground(fromProgressStatus(progressStatus));
                    break;

                case ABS:
                    binding.buttonAbs.setBackground(fromProgressStatus(progressStatus));
                    break;

                case LOWER_BACK:
                    binding.buttonLowerBack.setBackground(fromProgressStatus(progressStatus));
                    break;

                case QUADS:
                    binding.buttonQuadLeft.setBackground(fromProgressStatus(progressStatus));
                    binding.buttonQuadRight.setBackground(fromProgressStatus(progressStatus));
                    break;

                case HARM_STRINGS:
                    binding.buttonHarmStringLeft.setBackground(fromProgressStatus(progressStatus));
                    binding.buttonHarmStringRight.setBackground(fromProgressStatus(progressStatus));
                    break;

                case CALVES:
                    binding.buttonCalfLeft.setBackground(fromProgressStatus(progressStatus));
                    binding.buttonCalfRight.setBackground(fromProgressStatus(progressStatus));
                    break;

                default:
                    throw new IllegalStateException("Case not found for " + key);
            }
        });
    }

    private Drawable fromProgressStatus(ProgressStatus progressStatus) {

        if (getContext() == null)
            throw new IllegalStateException("Context is null. Why?");

        switch (progressStatus) {

            case DONE:
                return ContextCompat.getDrawable(getContext(), R.drawable.body_part_oval_green);

            case IN_PROGRESS:
                return ContextCompat.getDrawable(getContext(), R.drawable.body_part_oval_yellow);

            case EMPTY:
                return ContextCompat.getDrawable(getContext(), R.drawable.body_part_oval_red);

            default:
                throw new IllegalStateException("No case found for " + progressStatus);
        }
    }
}