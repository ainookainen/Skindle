package com.example.skindle.util;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.util.Random;

public class SplashArtCropper {
    private static int cx;
    private static int cy;


    public static Bitmap cropSplash(Bitmap bitmap, int viewSize, float zoom) {
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
