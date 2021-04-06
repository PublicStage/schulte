package com.bear.brain;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class InitInfo {

    @SerializedName("places")
    public HashMap<Integer, Integer> places;
    @SerializedName("users")
    public HashMap<Integer, Integer> users;
    @SerializedName("name")
    String name;
    @SerializedName("chatVersion")
    int chatMinVersion;
    @SerializedName("ratingVersion")
    int ratingMinVersion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer, Integer> getPlaces() {
        return places;
    }

    public HashMap<Integer, Integer> getUsers() {
        return users;
    }

    public void setUsers(HashMap<Integer, Integer> users) {
        this.users = users;
    }

    public void setPlaces(HashMap<Integer, Integer> places) {
        this.places = places;
    }

    public int getChatMinVersion() {
        return chatMinVersion;
    }

    public void setChatMinVersion(int chatMinVersion) {
        this.chatMinVersion = chatMinVersion;
    }

    public int getRatingMinVersion() {
        return ratingMinVersion;
    }

    public void setRatingMinVersion(int ratingMinVersion) {
        this.ratingMinVersion = ratingMinVersion;
    }
}
