package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class RItem {
    @SerializedName("name")
    String name;
    @SerializedName("score")
    long score;
    @SerializedName("owner")
    boolean owner;

    public RItem(String name, long score, boolean owner) {
        this.name = name;
        this.score = score;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public long getScore() {
        return score;
    }

    public boolean isOwner() {
        return owner;
    }
}
