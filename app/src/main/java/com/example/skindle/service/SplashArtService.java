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

    /* The code is from the accepted answer (by TWL) https://stackoverflow.com/questions/41104831/how-to-download-an-image-by-using-volley */
    public void setSplash(SplashArt splashArt, ImageView imageView, Context context, float zoom, TextView textView) {
        String url = splashArt.getImageUrl();
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.post(() -> {
                    int viewSize = imageView.getHeight();
                    Bitmap cropped = cropSplash(bitmap, viewSize, zoom);
                    imageView.setImageBitmap(cropped);
                });
            }
        }, 0, 0, imageView.getScaleType(), null, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                textView.setText(R.string.something_went_wrong);
            }
        });
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    public Bitmap cropSplash(Bitmap bitmap, int viewSize, float zoom) {
        int maxWidth = bitmap.getWidth();
        int maxHeight = bitmap.getHeight();
        int cropSize = (int) (zoom * viewSize);
        cropSize = Math.min(cropSize, Math.min(maxWidth, maxHeight));
        if (zoom == 0.25f) {
            int xBound = Math.max(maxWidth - cropSize, 0);
            int yBound = Math.max(maxHeight - cropSize, 0);
            cx = new Random().nextInt(xBound + 1) + cropSize / 2;
            cy = new Random().nextInt(yBound + 1) + cropSize / 2;
        }
        int x = cx - cropSize / 2;
        int y = cy - cropSize / 2;
        x = Math.max(0, Math.min(x, maxWidth - cropSize));
        y = Math.max(0, Math.min(y, maxHeight - cropSize));
        Bitmap cropped = Bitmap.createBitmap(bitmap, x, y, cropSize, cropSize);
        return Bitmap.createScaledBitmap(cropped, viewSize, viewSize, true);
    }
}
