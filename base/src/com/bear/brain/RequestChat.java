package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class RequestChat extends RequestCommand {
    @SerializedName("locale")
    String locale;
    @SerializedName("lastId")
    int lastId;
    @SerializedName("forward")
    boolean forward;

    public RequestChat(String locale, int lastId, boolean forward) {
        super("chat");
        this.locale = locale;
        this.lastId = lastId;
        this.forward = forward;
    }

    public String getLocale() {
        return locale;
    }

    public int getLastId() {
        return lastId;
    }

    public boolean isForward() {
        return forward;
    }
}
