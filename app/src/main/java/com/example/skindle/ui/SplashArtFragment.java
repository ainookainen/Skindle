package com.example.skindle.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skindle.R;
import com.example.skindle.data.IServiceCallback;
import com.example.skindle.data.SplashArtRepository;
import com.example.skindle.databinding.FragmentSplashArtBinding;
import com.example.skindle.model.Champion;
import com.example.skindle.model.SplashArt;
import com.example.skindle.service.SkindleGameService;
import com.example.skindle.service.SplashArtService;
import com.example.skindle.util.WrongGuessAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SplashArtFragment extends Fragment {
    private FragmentSplashArtBinding binding;
    private SplashArtService splashArtService;
    private SkindleGameService skindleGameService;
    private SplashArtRepository splashArtRepository;
    private ArrayAdapter<String> autoCompleteTextViewAdapter;
    private WrongGuessAdapter adapter;
    private RecyclerView recyclerView;
    private List<Champion> wrongGuesses;

    public SplashArtFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashArtBinding.inflate(inflater, container, false);
        wrongGuesses = new ArrayList<>();
        adapter = new WrongGuessAdapter(wrongGuesses, requireContext());
        recyclerView = binding.GuessRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        splashArtService = new SplashArtService(getContext());
        splashArtRepository = new SplashArtRepository(getContext());
        skindleGameService = new SkindleGameService(splashArtService, splashArtRepository);
        loadNewSplash();
        autoCompleteTextViewAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, skindleGameService.getGuessableChampions());
        binding.GuessButton.setOnClickListener(v -> {
            String guess = binding.GuessText.getText().toString().strip();
            checkGuess(guess, view);
        });
    }

    private void loadNewSplash() {
        skindleGameService.newSplash(new IServiceCallback() {
            @Override
            public <T> void onSuccess(T data) {
                SplashArt splashArt = skindleGameService.getSplashArt();
                splashArtRepository.setSplash(splashArt, binding.SplashImage, requireContext(), skindleGameService.getZoom(), binding.LoadingText);
                binding.SolveButton.setEnabled(true);
                binding.SolveButton.setAlpha(1f);
                binding.GuessButton.setEnabled(true);
                binding.SolveButton.setOnClickListener(v -> {
                    SplashArt solution = skindleGameService.getSplashArt();
                    binding.GuessText.setText(solution.getChampionName().toUpperCase(Locale.ENGLISH));
                });
                binding.GuessText.setAdapter(autoCompleteTextViewAdapter);
            }

            @Override
            public void onLoading() {
                binding.LoadingText.setText(R.string.loading);
                binding.SolveButton.setEnabled(false);
                binding.SolveButton.setAlpha(0.3f); // second answer in https://stackoverflow.com/questions/38186885/is-there-a-way-i-can-gray-out-an-imagebutton-in-android-without-maintaining-a-se
                binding.GuessButton.setEnabled(false);
            }

            @Override
            public void onError(Exception e) {
                binding.LoadingText.setText(R.string.something_went_wrong);
                binding.SolveButton.setEnabled(false);
                binding.SolveButton.setAlpha(0.3f);
                binding.GuessButton.setEnabled(false);
            }
        });
    }

    private void checkGuess(String guess, View v) {
        if (skindleGameService.isValidChampion(guess)) {
            boolean correct = skindleGameService.guess(guess);
            if (correct) {
                Snackbar.make(v, "Correct!", Snackbar.LENGTH_SHORT).show();
                loadNewSplash();
                wrongGuesses.clear();
                wrongGuesses.addAll(skindleGameService.getWrongGuesses());
                adapter.notifyDataSetChanged();
                updateAutoCompleteTextView();
            } else {
                Snackbar.make(v, "Try again", Snackbar.LENGTH_SHORT).show();
                SplashArt splashArt = skindleGameService.getSplashArt();
                splashArtRepository.setSplash(splashArt, binding.SplashImage, requireContext(), skindleGameService.getZoom(), binding.LoadingText);
                wrongGuesses.add(0, skindleGameService.getWrongGuesses().get(0)); // new item was inserted to idx 0 of wrongGuesses in Gameservice
                adapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
                updateAutoCompleteTextView();
            }
        } else {
            Snackbar.make(v, "Not a valid champion.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void updateAutoCompleteTextView() {
        binding.GuessText.setText("");
        autoCompleteTextViewAdapter.clear();
        autoCompleteTextViewAdapter.addAll(skindleGameService.getGuessableChampions());
        autoCompleteTextViewAdapter.notifyDataSetChanged();
    }
}