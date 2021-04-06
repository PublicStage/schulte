package com.etheapp.brainserver.db;

public class ResultRecord {
    public String uuid;
    public long date;
    public int level;
    public long result;

    public ResultRecord(String uuid, long date, int level, long result) {
        this.uuid = uuid;
        this.date = date;
        this.level = level;
        this.result = result;
    }
}
