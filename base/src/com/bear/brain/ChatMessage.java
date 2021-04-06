package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class ChatMessage {
    @SerializedName("time")
    long time;
    @SerializedName("name")
    String name;
    @SerializedName("message")
    String message;
    @SerializedName("place")
    int place;

    public ChatMessage(long time, String name, String message) {
        this.time = time;
        this.name = name;
        this.message = message;
        this.place = 0;
    }

    public ChatMessage(long time, String name, String message, int place) {
        this.time = time;
        this.name = name;
        this.message = message;
        this.place = place;
    }

    public long getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public int getPlace() {
        return place;
    }

    public boolean hasPlace() {
        return place > 0;
    }
}
