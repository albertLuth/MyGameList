package com.example.albert.myapplication;

public class Mission {
    private String Name;
    private String Level;

    public Mission(String name, String level) {
        Name = name;
        Level = level;
    }

    public String getName() {
        return Name;
    }

    public String getLevel() {
        return Level;
    }
}
