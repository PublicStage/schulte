package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class RequestEcho extends RequestCommand {
    @SerializedName("parameter")
    String parameter;

    public RequestEcho(String parameter) {
        super("echo");
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
