package com.example.skindle.model;

public class Champion {
    private String id;
    /**
     * Wukong has the id MonkeyKing...
     */
    private String name;
    private String iconUrl;

    public Champion(String id, String name) {
        this.id=id;
        this.name = name;
        this.iconUrl = "https://ddragon.leagueoflegends.com/cdn/15.12.1/img/champion/"+id+".png";
    }

    public String getName() {
        return name;
    }
    public String getId(){
        return id;
    }
    public String getIconUrl(){
        return iconUrl;
    }
}
