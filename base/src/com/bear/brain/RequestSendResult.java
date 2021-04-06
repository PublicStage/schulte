package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class RequestSendResult extends RequestCommand {
    @SerializedName("parameter")
    SendResult parameter;

    public RequestSendResult(SendResult parameter) {
        super("sendresult");
        this.parameter = parameter;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public SendResult getParameter() {
        return parameter;
    }

    public void setParameter(SendResult parameter) {
        this.parameter = parameter;
    }
}
