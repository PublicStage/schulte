package com.bear.brain.logic;

import com.badlogic.gdx.utils.Array;

public class TimesWithMax extends Times {

    Array<MaxListener> maxListeners = new Array<>();

    public TimesWithMax(int size) {
        super(size);
    }

    public void addMaxListener(MaxListener listener) {
        maxListeners.add(listener);
    }

    @Override
    public boolean setMaxTime(long maxTime) {
        if (super.setMaxTime(maxTime)) {
            for (MaxListener listener : maxListeners) listener.onMaxTimeChange(this.maxTime);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        super.clear();
        for (MaxListener listener : maxListeners) listener.onMaxTimeChange(maxTime);
    }
}
