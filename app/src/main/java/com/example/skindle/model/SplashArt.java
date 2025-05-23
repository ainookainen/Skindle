package com.example.skindle.model;

public class SplashArt {
    private int skinNum;
    private String imageUrl;


    public SplashArt(String championName, int skinNum) {
        this.imageUrl = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + championName + "_" + skinNum + ".jpg";
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
