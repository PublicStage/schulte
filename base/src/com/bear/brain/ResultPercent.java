package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class ResultPercent {
    @SerializedName("percent")
    int percent;
    @SerializedName("best")
    int best;

    public ResultPercent() {
    }

    public ResultPercent(int percent) {
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getBest() {
        return best;
    }

    public void setBest(int best) {
        this.best = best;
    }
}
