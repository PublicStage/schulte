package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class RequestCommand {
    @SerializedName("command")
    String command;

    public RequestCommand(String command) {
        this.command = command;

    }

    public String getCommand() {
        return command;
    }
}
