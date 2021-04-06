package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class ChangeNameResult {
    @SerializedName("type")
    ChangeNameType type;
    @SerializedName("name")
    String name;
    @SerializedName("places")
    Places places;

    public ChangeNameResult(ChangeNameType type) {
        this.type = type;
    }

    public ChangeNameResult(String name, Places places) {
        this.type = ChangeNameType.OK;
        this.name = name;
        this.places = places;
    }

    public ChangeNameType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Places getPlaces() {
        return places;
    }
}
