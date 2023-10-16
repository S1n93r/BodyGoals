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
import com.slinger.bodygoals.model.CalendarWeek;
import com.slinger.bodygoals.model.MuscleGroup;
import com.slinger.bodygoals.model.Progress;
import com.slinger.bodygoals.model.ProgressStatus;
import com.slinger.bodygoals.model.User;
import com.slinger.bodygoals.ui.ViewModel;

import java.util.Map;

public class CoverageFragment extends Fragment {

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
                NavHostFragment.findNavController(CoverageFragment.this).navigate(R.id.action_CoverageFragment_to_OverviewFragment));

        /* Switch calendar week component */
        binding.switchCalendarWeekComponent.setLifecycleOwner(this);

        binding.switchCalendarWeekComponent.setCalendarWeekLiveData(viewModel.getSelectedCalendarWeek());

        binding.switchCalendarWeekComponent.registerPreviousWeekButtonAction(() -> viewModel.selectPreviousWeek());
        binding.switchCalendarWeekComponent.registerNextWeekButtonAction(() -> viewModel.selectNextWeek());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void registerLiveDataObserver() {

        viewModel.getCurrentUser().observe(this,
                user -> updateCoverage(user, viewModel.getSelectedCalendarWeek().getValue()));

        viewModel.getSelectedCalendarWeek().observe(this, calendarWeek -> {

            User user = viewModel.getCurrentUser().getValue();

            /* TODO: Meh. */
            if (user == null)
                return;

            updateCoverage(user, calendarWeek);
        });
    }

    private void updateCoverage(User user, CalendarWeek calendarWeek) {

        Map<MuscleGroup, Progress> progressPerMuscleGroup = user.getSessionLog().progressPerMuscleGroup(calendarWeek);

        progressPerMuscleGroup.forEach((key, value) -> {

            ProgressStatus progressStatus = value.getProgressStatus();

            switch (key) {

                case NECK:
                    binding.buttonHead.setBackground(fromProgressStatus(progressStatus));
                    break;

                case CHEST:
                    binding.buttonChest.setBackground(fromProgressStatus(progressStatus));
                    break;

                /* TODO: Needs own part. */
                case LATS:
//                    bodyPartImageView = binding.buttonChest;
                    break;

                case SHOULDERS:
                    binding.buttonShoulderLeft.setBackground(fromProgressStatus(progressStatus));
                    binding.buttonShoulderRight.setBackground(fromProgressStatus(progressStatus));
                    break;

                case BICEPS:
                    binding.buttonArmLeft.setBackground(fromProgressStatus(progressStatus));
                    binding.buttonArmRight.setBackground(fromProgressStatus(progressStatus));
                    break;

                /* TODO: Needs own part. */
                case TRICEPS:
//                    binding.buttonArmLeft.setBackground(fromProgressStatus(progressStatus));
//                    binding.buttonArmRight.setBackground(fromProgressStatus(progressStatus));
                    break;

                case FOREARMS:
                    binding.buttonForearmLeft.setBackground(fromProgressStatus(progressStatus));
                    binding.buttonForearmRight.setBackground(fromProgressStatus(progressStatus));
                    break;

                case ABS:
                    binding.buttonCore.setBackground(fromProgressStatus(progressStatus));
                    break;

                /* TODO: Needs own part. */
                case LOWER_BACK:
//                    binding.buttonCore.setBackground(fromProgressStatus(progressStatus));
                    break;

                case QUADS:
                    binding.buttonLegLeft.setBackground(fromProgressStatus(progressStatus));
                    binding.buttonLegRight.setBackground(fromProgressStatus(progressStatus));
                    break;

                /* TODO: Needs own part. */
                case HARM_STRINGS:
//                    binding.buttonLegLeft.setBackground(fromProgressStatus(progressStatus));
//                    binding.buttonLegRight.setBackground(fromProgressStatus(progressStatus));
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