package com.example.albert.myapplication;


public class Game {
    private String Title;
    private String Platform;
    private String Year;
    private String Studio;
    private String State;

    public Game(String title, String platform, String year, String studio, String state) {
        Title = title;
        Platform = platform;
        Year = year;
        Studio = studio;
        State = state;
    }

    public String getTitle() {
        return Title;
    }

    public String getPlatform() {
        return Platform;
    }

    public String getYear() {
        return Year;
    }

    public String getStudio() {
        return Studio;
    }

    public String getState() {
        return State;
    }
}
