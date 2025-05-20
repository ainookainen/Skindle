package com.example.skindle.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.skindle.databinding.FragmentSplashArtBinding;
import com.github.mikephil.charting.charts.LineChart;

import org.jetbrains.annotations.Nullable;

public class SplashArtFragment extends Fragment {
    private FragmentSplashArtBinding binding;
    private LineChart lineChart;

    public SplashArtFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashArtBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }
}