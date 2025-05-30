package com.example.skindle.model;

public class Champion {
    private String id;
    /**
     * Wukong has the id MonkeyKing...
     */
    private String name;

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
}
