package com.example.skindle.service;

import com.example.skindle.data.IRepositoryCallback;
import com.example.skindle.data.IServiceCallback;
import com.example.skindle.data.SpellRepository;
import com.example.skindle.data.SplashArtRepository;
import com.example.skindle.model.Ability;
import com.example.skindle.model.Champion;

import java.util.ArrayList;
import java.util.Locale;

public class SpelldleGameService {
    private Ability ability;
    private final AbilityService abilityService;
    private final SplashArtRepository splashArtRepository;
    private final SpellRepository spellRepository;
    private ArrayList<Champion> allChampions = new ArrayList<>();
    private ArrayList<Champion> wrongGuesses = new ArrayList<>();
    private ArrayList<String> guessableChampions = new ArrayList<>();

    public SpelldleGameService(SplashArtRepository splashArtRepository, SpellRepository spellRepository, AbilityService abilityService) {
        this.abilityService = abilityService;
        this.splashArtRepository = splashArtRepository;
        this.spellRepository = spellRepository;
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

    public void newAbility(IServiceCallback callback) {

        abilityService.fetchAbilityImage(new IServiceCallback() {
            @Override
            public <T> void onSuccess(T data) {
                if (data instanceof Ability) {
                    ability = (Ability) data;
                    callback.onSuccess(ability);
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
        if (ability.getChampionName().equalsIgnoreCase(guess)) {
            wrongGuesses.clear();
            guessableChampions.clear();
            for (Champion champion : allChampions) {
                String name = champion.getName().toUpperCase(Locale.ENGLISH);
                guessableChampions.add(name);
            }
            return true;
        } else {
            guessableChampions.remove(guess);
            Champion guessC = getChampionWithName(guess);
            if (guessC != null) {
                wrongGuesses.add(0, guessC);
            }
            return false;
        }
    }


    public Ability getAbility() {
        return ability;
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
