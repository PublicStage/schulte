package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("uuid")
    public String uuid;

    @SerializedName("name")
    public String name;

}