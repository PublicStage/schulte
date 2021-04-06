package com.bear.brain.logic;

import com.google.gson.annotations.SerializedName;

public class LevelsTimes {
    @SerializedName("levels")
    Times[] levels;

    public LevelsTimes() {
        levels = new Times[State.MAX_LEVEL - State.MIN_LEVEL + 1];
        for (int level = State.MIN_LEVEL; level <= State.MAX_LEVEL; level++) {
            levels[level - State.MIN_LEVEL] = new Times(level * level);
        }
    }

    public Times get(int level) {
        return levels[level - State.MIN_LEVEL];
    }
}
