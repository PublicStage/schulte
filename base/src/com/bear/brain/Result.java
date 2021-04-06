package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("d")
    public long date;
    @SerializedName("t")
    public long duration;

    public Result() {
    }

    public Result(long date, long duration) {
        this.date = date;
        this.duration = duration;
    }
}
