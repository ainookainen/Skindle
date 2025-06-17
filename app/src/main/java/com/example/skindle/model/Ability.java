package com.example.skindle.model;

public class Ability {
    String abilityFull;
    String abilityName;
    String championName;
    String imageUrl;

    public Ability(String abilityFull, String abilityName, String championName) {
        this.abilityFull = abilityFull;
        this.abilityName = abilityName;
        this.championName = championName;
        this.imageUrl = "https://ddragon.leagueoflegends.com/cdn/15.12.1/img/spell/"+abilityFull;
    }
    public String getImageUrl(){
        return imageUrl;
    }
    public String getChampionName(){
        return championName;
    }
}
