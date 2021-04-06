package com.bear.brain.logic;

import com.badlogic.gdx.utils.Array;

public class PressTimer {
    TimesWithMax current;
    long actTime;

    long timer;
    State state;

    Array<TicListener> ticListeners = new Array<>();
    private LevelsTimes levels;

    public PressTimer(State state) {
        this.state = state;
        levels = new LevelsTimes();
        current = new TimesWithMax(State.MAX_LEVEL * State.MAX_LEVEL);
    }

    public LevelsTimes getLevels() {
        return levels;
    }

    public void setLevels(LevelsTimes levels) {
        this.levels = levels;
    }

    public void start() {
        current.clear();
        timer = State.deviceTime.getTime();
    }

    public void addMaxListener(MaxListener listener) {
        current.addMaxListener(listener);
    }

    public void addTicListener(TicListener listener) {
        ticListeners.add(listener);
    }

    public void press(int id) {
        long time = State.deviceTime.getTime();
        long delta = time - timer;
        current.set(id - 1, delta);
        timer = time;
    }

    public long timerValue() {
        return State.deviceTime.getTime() - timer;
    }

    public void act() {
        long time = State.deviceTime.getTime();
        if (time - actTime > 25) {
            actTime = time;
            long delta = time - timer;
            current.setMaxTime(delta);
            for (TicListener listener : ticListeners) listener.tic();
        }
    }

    public long getTime(int i) {
        return current.get(i);
    }

    public void stop() {
        int level = state.getSize();
        levels.get(level).copyFrom(current);
    }

    public void changeLevel(int level) {
        current.copyFrom(levels.get(level));
    }
}
