package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class RequestRatioList extends RequestCommand {
    @SerializedName("level")
    int level;
    @SerializedName("from")
    int from;
    @SerializedName("forward")
    boolean forward;
    @SerializedName("uuid")
    String uuid;

    public RequestRatioList(int level, int from, boolean forward, String uuid) {
        super("ratiolist");
        this.level = level;
        this.from = from;
        this.forward = forward;
        this.uuid = uuid;
    }

    public int getLevel() {
        return level;
    }

    public int getFrom() {
        return from;
    }

    public boolean isForward() {
        return forward;
    }

    public String getUuid() {
        return uuid;
    }
}
