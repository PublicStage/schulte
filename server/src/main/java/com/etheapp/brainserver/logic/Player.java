package com.etheapp.brainserver.logic;

import java.util.HashMap;

public class Player {
    public static final int MIN_LEVEL = 3;
    public static final int MAX_LEVEL = 5;

    private final String uuid;
    private final Scores[] scoresByLevel = new Scores[MAX_LEVEL - MIN_LEVEL + 1];
    private String name;

    public Player(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean hasName() {
        return name != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Scores addScore(int level, Score score) {
        Scores scores = getScores(level);
        if (scores == null) {
            scores = setScores(level);
        }
        scores.add(score);
        return scores;
    }

    public Scores getScores(int level) {
        return scoresByLevel[level - MIN_LEVEL];
    }

    private Scores setScores(int level) {
        scoresByLevel[level - MIN_LEVEL] = new Scores(this);
        return scoresByLevel[level - MIN_LEVEL];
    }

    public int getBestPlace() {
        int best = Integer.MAX_VALUE;
        for (Scores scores : scoresByLevel) {
            if (scores != null && scores.getPlace() < best) {
                best = scores.getPlace();
            }
        }
        return best;
    }

    public boolean hasScores(int level) {
        return getScores(level) != null;
    }

    public int getPlace(int level) {
        return hasScores(level) ? getScores(level).getPlace() : -1;
    }

    public HashMap<Integer, Integer> getPlacesByLevel() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int level = Player.MIN_LEVEL; level <= Player.MAX_LEVEL; level++) {
            if (hasScores(level)) {
                map.put(level, getScores(level).getPlace());
            }
        }
        return map;
    }
}
