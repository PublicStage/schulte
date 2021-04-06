package com.etheapp.brainserver.ranger;

import com.etheapp.brainserver.logic.Score;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String uuid;
    public long result = Long.MAX_VALUE;
    public long next = Long.MAX_VALUE;
    public List<Score> scores = new ArrayList<>();

    final Score BAD_SCORE = new Score(0, Long.MAX_VALUE);
    Score best = BAD_SCORE;

    public User(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public User(String uuid, long result) {
        this.uuid = uuid;
        this.result = result;
    }

    public void addScore(Score score) {
        scores.add(score);
        if (score.isBetter(best)) {
            best = score;
        }
    }

    public Score getBest() {
        return best;
    }

    public long getFirstScoreDate() {
        return scores.get(0).getDate();
    }

    public boolean isBetter(User other) {
        return best.isBetter(other.getBest());
    }

    public int compareResult(User other) {
        return isBetter(other) ? -1 : equal(other) ? 0 : 1;
    }

    public boolean equal(User other) {
        return best.equal(other.getBest());
    }

    public void removeScore(Score score) {
        scores.remove(score);
        if (score.equal(best)) {
            best = findBest();
        }
    }

    private Score findBest() {
        Score best = BAD_SCORE;
        for (Score score : scores) {
            if (score.isBetter(best)) {
                best = score;
            }
        }
        return best;
    }

    public boolean hasNoScores() {
        return scores.isEmpty();
    }

    public void removeFirstScore() {
        Score score = scores.remove(0);
        if (score.equal(best)) {
            best = findBest();
        }
    }
}
