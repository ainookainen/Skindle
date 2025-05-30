package com.example.skindle.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.skindle.model.Champion;
import com.example.skindle.model.SplashArt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashArtRepository {
    private Context context;

    public SplashArtRepository(Context context) {
        this.context = context;
    }

    public void getChampionsData(IRepositoryCallback callback) {
        String url = "https://ddragon.leagueoflegends.com/cdn/15.10.1/data/en_US/champion.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject championsData = response.getJSONObject("data");
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

    public void getSplashData(String championId, String championName, IRepositoryCallback callback) {
        String url = "https://ddragon.leagueoflegends.com/cdn/15.10.1/data/en_US/champion/" + championId + ".json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject data = response.getJSONObject("data");
                JSONObject champion = data.getJSONObject(championId);
                JSONArray skins = champion.getJSONArray("skins");
                ArrayList<SplashArt> splashes = new ArrayList<>();
                for (int i = 0; i < skins.length(); i++) {
                    JSONObject skin = skins.getJSONObject(i);
                    int skinNum = skin.getInt("num");
                    splashes.add(new SplashArt(championId, championName, skinNum));
                }
                callback.onSuccess(splashes);
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
