package com.example.skindle.service;

import com.example.skindle.data.IRepositoryCallback;
import com.example.skindle.data.IServiceCallback;
import com.example.skindle.data.SplashArtRepository;
import com.example.skindle.model.Champion;
import com.example.skindle.model.SplashArt;

import java.util.ArrayList;
import java.util.Locale;

public class SkindleGameService {
    private SplashArt splashArt;
    private float zoom;
    private final float zoomIncrease = 0.05f;
    private final SplashArtService splashArtService;
    private final SplashArtRepository splashArtRepository;
    private ArrayList<Champion> allChampions = new ArrayList<>();
    private ArrayList<Champion> wrongGuesses = new ArrayList<>();
    private ArrayList<String> guessableChampions = new ArrayList<>();

    public SkindleGameService(SplashArtService splashArtService, SplashArtRepository splashArtRepository) {
        this.splashArtService = splashArtService;
        this.splashArtRepository = splashArtRepository;
        this.zoom = 0.25f;
        getAllChampions(new IRepositoryCallback() {
            @Override
            public <T> ArrayList<Champion> onSuccess(T data) {
                return null;
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
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
            wrongGuesses.clear();
            guessableChampions.clear();
            for (Champion champion : allChampions) {
                String name = champion.getName().toUpperCase(Locale.ENGLISH);
                guessableChampions.add(name);
            }
            return true;
        } else {
            zoom = Math.min(zoom + zoomIncrease, 1.0f);
            guessableChampions.remove(guess);
            Champion guessC = getChampionWithName(guess);
            if (guessC != null) {
                wrongGuesses.add(0, guessC);
            }
            return false;
        }
    }

    public float getZoom() {
        return zoom;
    }

    public SplashArt getSplashArt() {
        return splashArt;
    }

    public Champion getChampionWithName(String name) {
        for (Champion champion : allChampions) {
            if (champion.getName().equalsIgnoreCase(name)) {
                return champion;
            }
        }
        return null;
    }

    public ArrayList<String> getGuessableChampions() {
        return guessableChampions;
    }

    public ArrayList<Champion> getWrongGuesses() {
        return wrongGuesses;
    }

    private void getAllChampions(IRepositoryCallback callback) {
        splashArtRepository.getChampionsData(new IRepositoryCallback() {
            @Override
            public <T> ArrayList<Champion> onSuccess(T data) {
                if (data instanceof ArrayList) {
                    ArrayList<?> dataAsArrayList = (ArrayList<?>) data;
                    if (!dataAsArrayList.isEmpty() && dataAsArrayList.get(0) instanceof Champion) {
                        @SuppressWarnings("unchecked") ArrayList<Champion> championList = (ArrayList<Champion>) dataAsArrayList;
                        allChampions = championList;
                        for (Champion champion : championList) {
                            String name = champion.getName().toUpperCase(Locale.ENGLISH);
                            guessableChampions.add(name);
                        }
                    }
                }
                return null;
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    public boolean isValidChampion(String guess) {
        return guessableChampions.contains(guess.toUpperCase(Locale.ENGLISH));
    }
}
