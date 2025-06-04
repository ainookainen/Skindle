package com.example.skindle.service;

import android.view.View;

import com.example.skindle.data.IServiceCallback;
import com.example.skindle.model.SplashArt;
import com.google.android.material.snackbar.Snackbar;

public class GameService {
    private SplashArt splashArt;
    private float zoom;
    private float zoomIncrease = 0.05f;
    private final SplashArtService splashArtService;

    public GameService(SplashArtService splashArtService) {
        this.splashArtService = splashArtService;
        this.zoom = 0.25f;
    }

    public void newSplash(IServiceCallback callback) {
        zoom = 0.25f;
        splashArtService.fetchSplashArt(new IServiceCallback() {
            @Override
            public <T> void onSuccess(T data) {
                if (data instanceof SplashArt) {
                    splashArt = (SplashArt) data;
                    callback.onSuccess(splashArt);
                }
            }

            @Override
            public void onLoading() {
                callback.onLoading();
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    public boolean guess(String guess) {
        if (splashArt.getChampionName().equalsIgnoreCase(guess)) {
            return true;
        } else {
            zoom = Math.min(zoom + zoomIncrease, 1.0f);
            return false;
        }
    }
    public float getZoom(){
        return zoom;
    }
    public SplashArt getSplashArt(){
        return splashArt;
    }
}
