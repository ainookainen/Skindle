package com.example.skindle.model;

public class SplashArt {
    private int skinNum;
    private String championId;
    private String imageUrl;
    private String championName;


    public SplashArt(String championId, String championName, int skinNum) {
        this.imageUrl = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + championId + "_" + skinNum + ".jpg";
        this.skinNum = skinNum;
        this.championId = championId;
        this.championName = championName;
    }

    public int getSkinNum() {
        return skinNum;
    }

    public String getChampionId() {
        return championId;
    }
    public String getChampionName(){
        return championName;
    }
    public String getImageUrl(){
        return imageUrl;
    }
}
