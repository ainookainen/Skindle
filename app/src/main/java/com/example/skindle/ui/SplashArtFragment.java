package com.example.skindle.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.skindle.R;
import com.example.skindle.data.IServiceCallback;
import com.example.skindle.data.VolleySingleton;
import com.example.skindle.databinding.FragmentSplashArtBinding;
import com.example.skindle.model.SplashArt;
import com.example.skindle.service.SplashArtService;

import org.jetbrains.annotations.Nullable;

public class SplashArtFragment extends Fragment {
    private FragmentSplashArtBinding binding;
    private SplashArtService splashArtService;

    public SplashArtFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashArtBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        splashArtService = new SplashArtService(getContext());
        splashArtService.fetchSplashArt(new IServiceCallback() {
            @Override
            public <T> void onSuccess(T data) {
                if (data instanceof SplashArt) {
                    SplashArt splashArt = (SplashArt) data;
                    setSplash(splashArt);
                }
            }

            @Override
            public void onLoading() {
                binding.LoadingText.setText(R.string.loading);
            }

            @Override
            public void onError(Exception e) {
                binding.SplashImage.setImageResource(R.drawable.error);
            }
        });
    }

    /* The code is from the accepted answer (by TWL) https://stackoverflow.com/questions/41104831/how-to-download-an-image-by-using-volley */
    public void setSplash(SplashArt splashArt) {
        ImageView imageView = binding.SplashImage;
        String url = splashArt.getImageUrl();
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, imageView.getScaleType(), null, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                imageView.setImageResource(R.drawable.error);
            }
        });
        VolleySingleton.getInstance(requireContext().getApplicationContext()).getRequestQueue().add(request);
    }
}