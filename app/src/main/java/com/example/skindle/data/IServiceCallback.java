package com.example.skindle.data;

public interface IServiceCallback {
    <T> void onSuccess(T data);

    void onLoading();

    void onError(Exception e);

}