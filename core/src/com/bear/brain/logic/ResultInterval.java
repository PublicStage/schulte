package com.bear.brain.logic;

import com.badlogic.gdx.utils.Array;
import com.bear.brain.GameResult;

import java.util.Comparator;

public class ResultInterval {
    public Array<GameResult> array = new Array<>();
    Comparator<GameResult> comparator = new Comparator<GameResult>() {
        @Override
        public int compare(GameResult result1, GameResult result2) {
            return (result1.getDateTime() < result2.getDateTime()) ? -1 : ((result1.getDateTime() == result2.getDateTime()) ? 0 : 1);
        }
    };

    public void addResult(int level, long result, long time) {
        array.add(new GameResult(level, time, result));
        sort();
    }

    public GameResult getResult(int pos) {
        return array.get(pos);
    }

    public void removeOld(long time) {
        Array.ArrayIterator<GameResult> itr = array.iterator();
        while (itr.hasNext() && itr.next().getDateTime() < time) itr.remove();
    }

    void sort() {
        array.sort(comparator);
    }

    public void clear() {
        array.clear();
    }

    public int size() {
        return array.size;
    }

    public boolean isEmpty() {
        return array.size == 0;
    }
}
