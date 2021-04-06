package com.bear.brain;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Places {
    @SerializedName("places")
    public HashMap<Integer, Integer> places = new HashMap<>();

    @SerializedName("users")
    public HashMap<Integer, Integer> users = new HashMap<>();

    public HashMap<Integer, Integer> getPlaces() {
        return places;
    }

    public HashMap<Integer, Integer> getUsers() {
        return users;
    }
}