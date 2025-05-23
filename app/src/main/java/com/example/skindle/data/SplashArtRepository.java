package com.example.skindle.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.skindle.model.Champion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashArtRepository {
    private Context context;

    public SplashArtRepository() {
        this.context = context.getApplicationContext();
    }

    public void getChampionsData(IRepositoryCallback callback) {
        String url = "https://ddragon.leagueoflegends.com/cdn/15.10.1/data/en_US/champion.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject championsData = response.getJSONObject("ChampionsData");
                ArrayList<Champion> champions = new ArrayList<>();
                for (int i = 0; i < championsData.length(); i++) {
                    String championKey = championsData.names().getString(i);
                    JSONObject champion = championsData.getJSONObject(championKey);
                    String championID = champion.getString("id");
                    String championName = champion.getString("name");
                    champions.add(new Champion(championID, championName));
                }
                callback.onSuccess(champions);
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onError(e);
            }
        }, e -> {
            e.printStackTrace();
            callback.onError(e);
        });
        VolleySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
    }
}
