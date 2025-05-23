package com.example.skindle.model;

public class SplashArt {
    private int skinNum;
    private String championName;

    public SplashArt(String championName, int skinNum) {
        this.skinNum = skinNum;
        this.championName = championName;
    }

    public int getSkinNum() {
        return skinNum;
    }

    public String getChampionName() {
        return championName;
    }
}
