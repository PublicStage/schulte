package com.bear.brain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class LevelsData {
    @SerializedName("map")
    public HashMap<Integer, ArrayList<Result>> map = new HashMap<>();

    @SerializedName("toserver")
    public ArrayList<GameResult> listToServer = new ArrayList<>();

    public void add(int level, long date, long duration) {
        if (!map.containsKey(level)) map.put(level, new ArrayList<Result>());
        map.get(level).add(new Result(date, duration));
    }
}
