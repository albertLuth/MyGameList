package com.example.albert.myapplication;

public class CharObjMis {
    private String Name;
    private int Level;
    private String Type;
    private String Title;
    private String State;

    public CharObjMis(String name, String type, int level, String title, String state) {
        Name = name;
        Level = level;
        Type = type;
        Title = title;
        State = state;
    }

    public String getName() {
        return Name;
    }

    public int getLevel() {
        return Level;
    }

    public String getType() {
        return Type;
    }

    public String getTitle() {
        return Title;
    }

    public String getState() { return State; }
}
