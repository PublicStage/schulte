package com.etheapp.brainserver.bot;

import com.etheapp.brainserver.db.BotRecord;
import com.etheapp.brainserver.logic.GGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class BotScheduler {
    public static final long PERIOD = TimeUnit.MINUTES.toMillis(20);
    private final List<Bot> list = new ArrayList<>();
    private Timer timer;
    private int nextBot = 0;

    public BotScheduler(List<BotRecord> bots, GGame game) {
        for (BotRecord record : bots) {
            list.add(new Bot(record.getUuid(), record.getName(), record.getEffect(), game));
        }
    }

    private Bot getNextBot() {
        if (nextBot >= list.size()) {
            nextBot = 0;
        }
        return list.get(nextBot++);
    }

    public void start() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getNextBot().createResult();
                start();
            }
        }, PERIOD);
    }

    public void stop() {
        timer.cancel();
    }

}
