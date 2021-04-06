package com.bear.brain.logic;

import com.bear.brain.GameResult;

public class ValBound {
    long min = Long.MAX_VALUE;
    int minIndex;
    int maxIndex;
    long max = 0;

    long average;
    long sko = 0;

    State state;

    public ValBound(State state) {
        this.state = state;
    }

    public long getMin() {
        return min;
    }

    public boolean hasResult() {
        return min < Long.MAX_VALUE;
    }

    public int getMinIndex() {
        return minIndex;
    }

    public int getMaxIndex() {
        return maxIndex;
    }

    public boolean minValueIsLast() {
        int resultsCount = state.getResultsCount();
        return getMinIndex() == resultsCount - 1;
    }

    public boolean maxValueIsLast() {
        int resultsCount = state.getResultsCount();
        return getMaxIndex() == resultsCount - 1;
    }


    public long getMax() {
        return max;
    }

    public long getAverage() {
        return average;
    }

    public long getSko() {
        return sko;
    }

    public void updateMinMax() {
        min = Long.MAX_VALUE;
        max = 0;
        average = 0;
        sko = 0;
        for (int i = 0; i < state.getResultsCount(); i++) {
            GameResult gameResult = state.getGameResult(i);
            average += gameResult.getDuration();
            if (gameResult.getDuration() > max) {
                max = gameResult.getDuration();
                maxIndex = i;
            }
            if (gameResult.getDuration() < min) {
                min = gameResult.getDuration();
                minIndex = i;
            }
        }
        if (state.getResultsCount() > 0) {
            average /= state.getResultsCount();
            sko = findSKO();
            max = (long) Math.min(max, average + 1.5f * sko);
        }


    }

    private long findSKO() {
        long sum = 0;
        for (int i = 0; i < state.getResultsCount(); i++) {
            GameResult gameResult = state.getGameResult(i);
            long diff = gameResult.getDuration() - average;
            sum += diff * diff;
        }
        sum /= state.getResultsCount();
        sum = (long) Math.sqrt(sum);
        return sum;
    }

    public long getBound(long duration) {
        if (duration < min) return min;
        if (duration > max) return max;
        return duration;
    }

    public float normal(long duration) {
        return (float) (max - duration) / (max - min);
    }

    public float nor(int position) {
        long duration = state.getGameResult(position).getDuration();
        return duration > max ? 1 : (float) duration / max;
    }

}
