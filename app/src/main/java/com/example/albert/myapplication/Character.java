package com.example.albert.myapplication;

public class Character {
    private String Name;
    private String Race;
    private String Level;

    public Character(String name, String race, String level) {
        Name = name;
        Race = race;
        Level = level;
    }

    public String getName() {
        return Name;
    }

    public String getRace() {
        return Race;
    }

    public String getLevel() {
        return Level;
    }
}
