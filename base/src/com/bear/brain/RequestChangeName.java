package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class RequestChangeName extends RequestCommand {
    @SerializedName("uuid")
    String uuid;
    @SerializedName("name")
    String name;

    public RequestChangeName(String uuid, String name) {
        super("changename");
        this.uuid = uuid;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}
