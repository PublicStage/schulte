package com.bear.brain;

import com.google.gson.annotations.SerializedName;

public class Block {
    @SerializedName("chatNeedUpdate")
    public boolean chatNeedUpdate;

    @SerializedName("ratingNeedUpdate")
    public boolean ratingNeedUpdate;
}
