package com.bear.brain.logic;

import com.bear.brain.GameResult;

public class ValRange extends ValBound {
    public ValRange(State state) {
        super(state);
    }

    public float getPositionOfResultIndex(int index, float height) {
        GameResult gameResult = state.getGameResult(index);
        return (height - 1) * normal(getBound(gameResult.getDuration()));
    }


}
