package com.example.skindle.service;


import com.example.skindle.data.IRepositoryCallback;
import com.example.skindle.data.IServiceCallback;
import com.example.skindle.data.SplashArtRepository;
import com.example.skindle.model.SplashArt;

import kotlin.NotImplementedError;

public class SplashArtService {

    private final SplashArtRepository splashArtRepository = new SplashArtRepository();

    public SplashArt fetchSplashArt(String championName, String skinNum, IServiceCallback callback) {
        callback.onLoading();
        splashArtRepository.getChampionsData(new IRepositoryCallback() {
            @Override
            public <T> void onSuccess(T data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
        throw new NotImplementedError();
    }
}
