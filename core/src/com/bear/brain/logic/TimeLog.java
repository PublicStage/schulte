package com.bear.brain.logic;

import com.bear.brain.GameResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeLog {

    public static final long DAY = TimeUnit.DAYS.toMillis(1);

    public Map<Integer, ResultInterval> logByLevel = new HashMap<>();

    public void addGameResult(int level, long timeResult) {
        if (!logByLevel.containsKey(level)) logByLevel.put(level, new ResultInterval());
        logByLevel.get(level).addResult(level, timeResult, State.deviceTime.getTime());
    }

    public GameResult getGameResult(int level, int pos) {

        return logByLevel.get(level).getResult(pos);
    }

    public int gamesCount(int level) {
        return logByLevel.containsKey(level) ? logByLevel.get(level).size() : 0;
    }

    public void loadGameResult(int level, long date, long duration) {
        if (!logByLevel.containsKey(level)) logByLevel.put(level, new ResultInterval());
        logByLevel.get(level).addResult(level, duration, date);
    }

    public void sort(int level) {
        if (logByLevel.containsKey(level)) logByLevel.get(level).sort();
    }

    public void reset() {
        logByLevel.clear();
    }

    public void removeOld(int level) {
        if (logByLevel.containsKey(level))
            logByLevel.get(level).removeOld(State.deviceTime.getTime() - DAY);
    }
}
