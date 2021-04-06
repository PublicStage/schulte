package com.bear.brain.logic;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Times {
    @SerializedName("times")
    long[] times;
    @SerializedName("maxTime")
    long maxTime;

    public Times(int size) {
        times = new long[size];
    }

    public long get(int id) {
        return times[id];
    }

    public void set(int id, long value) {
        times[id] = value;
        setMaxTime(value);
    }

    public boolean setMaxTime(long maxTime) {
        if (maxTime > this.maxTime) {
            int seconds = (int) Math.max(1, Math.ceil(maxTime / 1000f)) * 1000;

            if (this.maxTime != seconds) {
                this.maxTime = seconds;
                return true;
            }
        }
        return false;
    }


    public void clear() {
        Arrays.fill(times, 0);
        maxTime = 1000;
    }

    public void copyFrom(Times current) {
        clear();
        for (int i = 0, count = Math.min(size(), current.size()); i < count; i++) {
            times[i] = current.get(i);
        }
        setMaxTime(current.maxTime);
    }

    private int size() {
        return times.length;
    }

}
