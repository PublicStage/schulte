package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class RequestLogin extends RequestCommand {
    @SerializedName("uuid")
    String uuid;
    @SerializedName("version")
    int version;

    public RequestLogin(String uuid, int version) {
        super("login");
        this.uuid = uuid;
        this.version = version;
    }

    public String getUuid() {
        return uuid;
    }

    public int getVersion() {
        return version;
    }
}
