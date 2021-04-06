package com.bear.brain.logic;

import com.bear.brain.GameResult;

public interface StateListener {
    void start();

    void finish(GameResult result);

    void press(int id);

    void changeLevel(int level);
}
