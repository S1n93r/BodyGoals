package com.slinger.bodygoals.ui.fragments;

import android.os.Bundle;
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

    private final MutableLiveData<Integer> currentSets = new MutableLiveData<>();
    private final MutableLiveData<Integer> maxSets = new MutableLiveData<>();

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

            binding.buttonToAddSession.setOnClickListener(addSessionView ->
                    NavHostFragment.findNavController(RunSession.this).navigate(
                            R.id.action_run_session_fragment_to_AddSessionFragment));
        });

        binding.buttonAddSet.setOnClickListener(runSessionView -> {

            if (currentSets.getValue() == null || maxSets.getValue() == null)
                return;

            if (currentSets.getValue() < maxSets.getValue())
                currentSets.setValue(currentSets.getValue() + 1);
        });
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
                binding.buttonDone.setActivated(maxSets.getValue().intValue() == currentSets);
        });

        maxSets.observe(this, maxSets -> {

            if (currentSets.getValue() != null && maxSets < currentSets.getValue())
                currentSets.setValue(maxSets);

            binding.buttonDone.setActivated(maxSets == currentSets.getValue().intValue());
        });
    }
}