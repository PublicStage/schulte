package com.bear.brain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SendResult {
    @SerializedName("uuid")
    public String uuid;
    @SerializedName("sendTime")
    public long sendTime;
    @SerializedName("list")
    public ArrayList<GameResult> list;

    public SendResult(String uuid, long sendTime, ArrayList<GameResult> list) {
        this.uuid = uuid;
        this.sendTime = sendTime;
        this.list = list;
    }

    public void timeCorrect(long serverTime) {
        long delta = serverTime - sendTime;
        sendTime = serverTime;
        for (GameResult result : list) {
            result.setDateTime(Math.min(serverTime, result.getDateTime() + delta));
        }
    }
}
