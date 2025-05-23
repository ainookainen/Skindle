package com.example.skindle.data;

import com.example.skindle.model.Champion;

import java.util.ArrayList;

public interface Callback {
    void onSuccess();

    void onFailure();

    void onLoading();

    void onError();
}
