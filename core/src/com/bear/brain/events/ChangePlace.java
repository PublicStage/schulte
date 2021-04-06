package com.bear.brain.events;

public class ChangePlace {
    int level;
    int place;

    public ChangePlace(int level, int place) {
        this.level = level;
        this.place = place;
    }

    public int getLevel() {
        return level;
    }

    public int getPlace() {
        return place;
    }
}
