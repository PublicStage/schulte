package com.etheapp.brainserver.logic;

public class Score {
    static final Score NULL = new Score(0, Long.MAX_VALUE);
    private final long date;
    private final long value;

    public Score(long date, long value) {
        this.date = date;
        this.value = value;
    }

    public long getDate() {
        return date;
    }

    public long getValue() {
        return value;
    }

    public boolean isBetter(Score other) {
        return value < other.getValue();
    }

    public boolean equal(Score other) {
        return value == other.getValue();
    }
}
