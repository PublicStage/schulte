package com.bear.brain.net;

import com.badlogic.gdx.utils.Array;
import com.bear.brain.ResultPercent;

public class NetListeners implements ResultPercentListener {
    Array<ResultPercentListener> resultPercentListeners = new Array<>();


    public void addResultPercentListener(ResultPercentListener listener) {
        resultPercentListeners.add(listener);
    }

    @Override
    public void handleResultPercent(ResultPercent resultPercent) {
        for (ResultPercentListener listener : resultPercentListeners) {
            listener.handleResultPercent(resultPercent);
        }
    }
}
