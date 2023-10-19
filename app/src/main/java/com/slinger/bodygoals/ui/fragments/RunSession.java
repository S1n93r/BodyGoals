package com.slinger.bodygoals.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.slinger.bodygoals.R;
import com.slinger.bodygoals.databinding.FragmentRunSessionBinding;
import com.slinger.bodygoals.ui.ViewModel;

public class RunSession extends Fragment {

    private final MutableLiveData<Integer> currentSets = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> maxSets = new MutableLiveData<>(0);

    private ViewModel viewModel;

    private FragmentRunSessionBinding binding;

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

        binding = FragmentRunSessionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonDone.setOnClickListener(runSessionView -> {

            viewModel.addSessions(viewModel.getPreSavedSessions());

            NavHostFragment.findNavController(RunSession.this).navigate(
                    R.id.action_run_session_fragment_to_OverviewFragment);
        });

        binding.buttonCancel.setOnClickListener(addSessionView ->
                NavHostFragment.findNavController(RunSession.this).navigate(
                        R.id.action_run_session_fragment_to_AddSessionFragment));

        binding.buttonAddSet.setOnClickListener(runSessionView -> {

            if (currentSets.getValue() == null || maxSets.getValue() == null)
                return;

            if (currentSets.getValue() < maxSets.getValue())
                currentSets.setValue(currentSets.getValue() + 1);
        });

        binding.editMaxSets.addTextChangedListener(new MaxSetsChangedWatcher());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void registerLiveDataObserver() {

        currentSets.observe(this, currentSets -> {

            binding.textCurrentSets.setText(String.valueOf(currentSets));

            if (maxSets.getValue() != null)
                binding.buttonDone.setEnabled(maxSets.getValue().intValue() == currentSets);
        });

        maxSets.observe(this, maxSets -> {

            if (currentSets.getValue() != null && maxSets < currentSets.getValue())
                currentSets.setValue(maxSets);

            binding.buttonDone.setEnabled(maxSets == currentSets.getValue().intValue());
        });
    }

    private class MaxSetsChangedWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            maxSets.setValue(0);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            /* TODO: Implement number guards. */
            if (count > 0)
                maxSets.setValue(Integer.parseInt(s.toString()));
        }

        @Override
        public void afterTextChanged(Editable s) {
            /* Currently not needed. */
        }
    }
}