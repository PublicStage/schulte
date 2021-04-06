package com.bear.brain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RPage {
    @SerializedName("list")
    ArrayList<RItem> list;
    @SerializedName("from")
    int from;
    @SerializedName("length")
    int length;
    @SerializedName("level")
    int level;
    @SerializedName("place")
    int place;
    @SerializedName("users")
    int users;

    public RPage(ArrayList<RItem> list, int from, int length) {
        this.list = list;
        this.from = from;
        this.length = length;
        place = -1;
    }

    public ArrayList<RItem> getList() {
        return list;
    }

    public int getFrom() {
        return from;
    }

    public int getLength() {
        return length;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }
}
