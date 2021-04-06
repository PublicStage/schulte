package com.bear.brain.logic;

import com.bear.brain.GameResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResultsLog implements StateListener {
    @SerializedName("results")
    ArrayList<GameResult> list = new ArrayList<>();

    @Override
    public void start() {

    }

    @Override
    public void finish(GameResult result) {
        if (result.isValid()) {
            list.add(result);
        }
    }

    @Override
    public void press(int id) {

    }

    @Override
    public void changeLevel(int level) {

    }

    public int size() {
        return list.size();
    }

    public GameResult getResult(int position) {
        return list.get(position);
    }

    public void copyFrom(ResultsLog log) {
        list.clear();
        list.addAll(log.list);
    }

    public void reset() {
        list.clear();
    }
}
