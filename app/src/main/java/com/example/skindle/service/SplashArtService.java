package com.example.skindle.service;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.skindle.R;
import com.example.skindle.data.IRepositoryCallback;
import com.example.skindle.data.IServiceCallback;
import com.example.skindle.data.SplashArtRepository;
import com.example.skindle.data.VolleySingleton;
import com.example.skindle.model.Champion;
import com.example.skindle.model.SplashArt;

import java.util.ArrayList;
import java.util.Random;

public class SplashArtService {
    private int cx;
    private int cy;

    private final SplashArtRepository splashArtRepository;

    public SplashArtService(Context context) {
        this.splashArtRepository = new SplashArtRepository(context);
    }


    public void fetchSplashArt(IServiceCallback callback) {
        callback.onLoading();
        splashArtRepository.getChampionsData(new IRepositoryCallback() {
            @Override
            public <T> void onSuccess(T data) {
                if (data instanceof ArrayList) {
                    ArrayList<?> dataAsArrayList = (ArrayList<?>) data;
                    if (!dataAsArrayList.isEmpty() && dataAsArrayList.get(0) instanceof Champion) {
                        @SuppressWarnings("unchecked")
                        ArrayList<Champion> championList = (ArrayList<Champion>) dataAsArrayList;
                        Champion champion = championList.get(new Random().nextInt(championList.size()));
                        String championId = champion.getId();
                        String championName = champion.getName();
                        splashArtRepository.getSplashData(championId, championName, new IRepositoryCallback() {
                            @Override
                            public <T> void onSuccess(T data) {
                                if (data instanceof ArrayList) {
                                    ArrayList<?> sDataAsArrayList = (ArrayList<?>) data;
                                    if (!sDataAsArrayList.isEmpty() && sDataAsArrayList.get(0) instanceof SplashArt) {
                                        @SuppressWarnings("unchecked")
                                        ArrayList<SplashArt> splashArtList = (ArrayList<SplashArt>) sDataAsArrayList;
                                        SplashArt splashArt = splashArtList.get(new Random().nextInt(splashArtList.size()));
                                        callback.onSuccess(splashArt);
                                    }
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                callback.onError(e);
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }
}
