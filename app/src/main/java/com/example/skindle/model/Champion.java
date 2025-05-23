package com.example.skindle.model;

import java.util.ArrayList;
import java.util.List;

public class Champion {
    private String id;
    /**
     * Wukong has the id MonkeyKing...
     */
    private String name;
    private ArrayList<SplashArt> skins = new ArrayList<>();

    public Champion(String id, String name) {
        this.id=id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getId(){
        return id;
    }
    public ArrayList<SplashArt> getSkins(){
        return skins;
    }
}
