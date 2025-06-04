package com.example.skindle.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.skindle.R;
import com.example.skindle.data.IServiceCallback;
import com.example.skindle.databinding.FragmentSplashArtBinding;
import com.example.skindle.model.SplashArt;
import com.example.skindle.service.GameService;
import com.example.skindle.service.SplashArtService;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Nullable;

public class SplashArtFragment extends Fragment {
    private FragmentSplashArtBinding binding;
    private SplashArtService splashArtService;
    private GameService gameService;

    public SplashArtFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashArtBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        splashArtService = new SplashArtService(getContext());
        gameService = new GameService(splashArtService);
        loadNewSplash();
        binding.GuessButton.setOnClickListener(v -> {
            String guess = binding.GuessText.getText().toString();
            boolean correct = gameService.guess(guess);
            if (correct) {
                Snackbar.make(v, "Correct!", Snackbar.LENGTH_SHORT).show();
                loadNewSplash();
                binding.GuessText.setText("");
            } else {
                SplashArt splashArt = gameService.getSplashArt();
                splashArtService.setSplash(splashArt, binding.SplashImage, requireContext(), gameService.getZoom(), binding.LoadingText);
                Snackbar.make(v, "Try again", Snackbar.LENGTH_SHORT).show();
            }
        });
        binding.SolveButton.setOnClickListener(v -> {
            SplashArt solution = gameService.getSplashArt();
            binding.GuessText.setText(solution.getChampionName());
        });
    }

    public void loadNewSplash() {
        gameService.newSplash(new IServiceCallback() {
            @Override
            public <T> void onSuccess(T data) {
                SplashArt splashArt = gameService.getSplashArt();
                splashArtService.setSplash(splashArt, binding.SplashImage, requireContext(), gameService.getZoom(), binding.LoadingText);
            }

            @Override
            public void onLoading() {
                binding.LoadingText.setText(R.string.loading);
            }

            @Override
            public void onError(Exception e) {
                binding.LoadingText.setText(R.string.something_went_wrong);
            }
        });
    }
}