package com.example.skindle.data;

public interface IRepositoryCallback {
    <T> void onSuccess(T data);
    void onError(Exception e);
}
