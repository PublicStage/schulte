package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class GameResult {
    @SerializedName("level")
    int level;
    @SerializedName("date")
    long dateTime;
    @SerializedName("duration")
    long duration;

    public GameResult() {
    }

    public GameResult(int level, long dateTime, long duration) {
        this.level = level;
        this.dateTime = dateTime;
        this.duration = duration;
    }

    public int getLevel() {
        return level;
    }

    public long getDuration() {
        return duration;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "[" + level + "] " + dateTime + "," + duration;
    }

    public boolean isValid() {
        return level != 0 && dateTime != 0 && duration != 0;
    }
}
