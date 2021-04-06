package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class SendMessage extends RequestCommand {
    @SerializedName("uuid")
    String uuid;
    @SerializedName("locale")
    String locale;
    @SerializedName("message")
    String message;

    public SendMessage(String uuid, String locale, String text) {
        super("sendMessage");
        this.uuid = uuid;
        this.locale = locale;
        this.message = text;
    }

    public String getUuid() {
        return uuid;
    }

    public String getLocale() {
        return locale;
    }

    public String getMessage() {
        return message;
    }
}
