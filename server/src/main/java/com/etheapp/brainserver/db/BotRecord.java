package com.etheapp.brainserver.db;

public class BotRecord {
    String uuid;
    String name;
    float effect;

    public BotRecord(String uuid, String name, float effect) {
        this.uuid = uuid;
        this.name = name;
        this.effect = effect;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public float getEffect() {
        return effect;
    }
}
