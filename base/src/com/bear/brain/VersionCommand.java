package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class VersionCommand {
    @SerializedName("value")
    int value;

    public VersionCommand(int value) {
        this.value = value;
    }
}