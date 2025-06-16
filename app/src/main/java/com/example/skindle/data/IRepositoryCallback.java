package com.example.skindle.data;

import com.example.skindle.model.Champion;

import java.util.ArrayList;

public interface IRepositoryCallback {
    <T> ArrayList<Champion> onSuccess(T data);
    void onError(Exception e);
}
