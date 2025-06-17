package com.example.skindle.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.skindle.R;
import com.example.skindle.model.Ability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class SpellRepository {
    private Context context;

    public SpellRepository(Context context) {
        this.context = context;
    }

    public void getAbilityData(String championId, String championName, IRepositoryCallback callback) {
        String url = "https://ddragon.leagueoflegends.com/cdn/15.12.1/data/en_US/champion/" + championId + ".json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject data = response.getJSONObject("data");
                JSONObject champion = data.getJSONObject(championId);
                JSONArray spells = champion.getJSONArray("spells");
                ArrayList<Ability> abilities = new ArrayList<>();
                for (int i = 0; i < spells.length(); i++) {
                    JSONObject spell = spells.getJSONObject(i);
                    String abilityName = spell.getString("name");
                    JSONObject imageObj = spell.getJSONObject("image");
                    String abilityFull= imageObj.getString("full");
                    abilities.add(new Ability(abilityFull, abilityName, championName));
                }
                callback.onSuccess(abilities);
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
    public void setAbilityImage(Ability ability, ImageView imageView, Context context, TextView textView){
        String url = ability.getImageUrl();
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.post(() -> {
                    imageView.setImageBitmap(bitmap);
                    Float[] floats = new Float[]{0f, 90f, 180f, 270f};
                    imageView.setRotation(floats[new Random().nextInt(floats.length)]);
                    imageView.setVisibility(View.VISIBLE);
                });
            }
        }, 0, 0, imageView.getScaleType(), null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText(R.string.something_went_wrong);
            }
        });
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }
}
