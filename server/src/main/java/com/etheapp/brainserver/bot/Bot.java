package com.etheapp.brainserver.bot;

import com.bear.brain.GameResult;
import com.etheapp.brainserver.db.DBService;
import com.etheapp.brainserver.logic.GGame;
import com.etheapp.brainserver.logic.Score;

public class Bot {
    String uuid;
    String name;
    float effect;
    long average[] = new long[]{5000, 16000, 60000};

    GGame game;

    public Bot(String uuid, String name, float effect, GGame game) {
        this.uuid = uuid;
        this.name = name;
        this.effect = effect;
        this.game = game;

        game.changeName(uuid, name);
    }

    public void createResult() {
        int level = randomLevel();
        Score score = new Score(game.getServerTime().currentTime(), generateTime(level));
        game.addScore(uuid, level, score);
        DBService.getInstance().writeResult(uuid, new GameResult(level, score.getDate(), score.getValue()));
    }

    private int randomLevel() {
        return (int) (3 + Math.random() * 3);
    }

    private long generateTime(int level) {
        long min = game.getTopContainer().get(level).getMinTime();
        long max = game.getTopContainer().get(level).getMaxTime();
        if (min == 0 || min > average[level - 3]) {
            min = (long) (average[level - 3] * (1 + Math.random() * 0.1f));
        }
        if (max <= min * 1.1f) {
            max = min * 2;
        }

        long length = max - min;
        return (long) (min + (1 - effect) * length + Math.random() * length * effect);
    }
}
