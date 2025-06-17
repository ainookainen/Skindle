package com.example.skindle.service;

import android.content.Context;

import com.example.skindle.data.IRepositoryCallback;
import com.example.skindle.data.IServiceCallback;
import com.example.skindle.data.SpellRepository;
import com.example.skindle.data.SplashArtRepository;
import com.example.skindle.model.Ability;
import com.example.skindle.model.Champion;

import java.util.ArrayList;
import java.util.Random;

public class AbilityService {
    private final SplashArtRepository splashArtRepository;

    private final SpellRepository spellRepository;

    public AbilityService(Context context) {
        this.splashArtRepository = new SplashArtRepository(context);
        this.spellRepository = new SpellRepository(context);
    }


    public void fetchAbilityImage(IServiceCallback callback) {
        callback.onLoading();
        splashArtRepository.getChampionsData(new IRepositoryCallback() {
            @Override
            public <T> ArrayList<Champion> onSuccess(T data) {
                if (data instanceof ArrayList) {
                    ArrayList<?> dataAsArrayList = (ArrayList<?>) data;
                    if (!dataAsArrayList.isEmpty() && dataAsArrayList.get(0) instanceof Champion) {
                        @SuppressWarnings("unchecked")
                        ArrayList<Champion> championList = (ArrayList<Champion>) dataAsArrayList;
                        Champion champion = championList.get(new Random().nextInt(championList.size()));
                        String championId = champion.getId();
                        String championName = champion.getName();
                        spellRepository.getAbilityData(championId, championName, new IRepositoryCallback() {
                            @Override
                            public <T> ArrayList<Champion> onSuccess(T data) {
                                if (data instanceof ArrayList) {
                                    ArrayList<?> aDataAsArrayList = (ArrayList<?>) data;
                                    if (!aDataAsArrayList.isEmpty() && aDataAsArrayList.get(0) instanceof Ability) {
                                        @SuppressWarnings("unchecked")
                                        ArrayList<Ability> abilityList = (ArrayList<Ability>) aDataAsArrayList;
                                        Ability ability = abilityList.get(new Random().nextInt(abilityList.size()));
                                        callback.onSuccess(ability);
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
                }
                return null;
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }
}
