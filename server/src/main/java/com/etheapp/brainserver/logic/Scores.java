package com.etheapp.brainserver.logic;

import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Scores {
    public static final int NO_PLACE = -1;
    private final Deque<Score> queue = new ConcurrentLinkedDeque<>();
    private Score best = Score.NULL;

    Player player;
    int place = NO_PLACE;

    public Scores(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Collection<Score> getList() {
        return queue;
    }

    public Score getBest() {
        return best;
    }

    public void add(Score score) {
        queue.add(score);
        best = findBest();
    }

    private Score findBest() {
        Score best = Score.NULL;
        for (Score score : queue) {
            if (score.isBetter(best)) {
                best = score;
            }
        }
        return best;
    }

    public int compareResult(Scores other) {
        return best.isBetter(other.getBest()) ? -1 : best.equal(other.getBest()) ? 0 : 1;
    }

    public int size() {
        return queue.size();
    }

    public boolean hasNoScores() {
        return queue.isEmpty();
    }

    public long getFirstScoreDate() {
        return queue.getFirst().getDate();
    }

    public void removeFirstScore() {
        Score score = queue.removeFirst();
        if (score.equal(best)) {
            best = findBest();
        }
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public void resetPlace() {
        place = NO_PLACE;
    }
}
