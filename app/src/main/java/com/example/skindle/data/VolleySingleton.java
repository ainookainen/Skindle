package com.example.skindle.data;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * App makes constant use of the Network, so a singleton class is the recommended approach in Google Volley documentation.
 * This class is a copy of the answer by Nicolás Carrasco-Stevenson:
 * https://stackoverflow.com/questions/47941438/proper-way-to-build-a-volley-singleton
 */
public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
