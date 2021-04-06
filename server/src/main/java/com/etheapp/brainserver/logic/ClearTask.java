package com.etheapp.brainserver.logic;

import com.etheapp.brainserver.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ClearTask {
    public static final long PERIOD = TimeUnit.DAYS.toMillis(1);
    Timer timer;
    ClearDate clearDate;
    ServerTime serverTime;
    long currentTimerValue = 0;

    public ClearTask(Timer timer, ClearDate clearDate, ServerTime serverTime) {
        this.timer = timer;
        this.clearDate = clearDate;
        this.serverTime = serverTime;
    }

    public void startTask() {
        long firstDate = clearDate.getFirstDateOrZero();
        if (firstDate != 0 && (timerIsStopped() || dateBeforeCurrentTimer(firstDate))) {

            if (!timerIsStopped()) {
                timer.cancel();
                timer = new Timer();
            }

            currentTimerValue = firstDate;
            long removeTime = currentTimerValue + PERIOD;
            long deltaTime = removeTime - serverTime.currentTime();
            Log.info("schedule timer on %s ms", Log.timeString(removeTime));
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    clearDate.removeEventsBefore(serverTime.currentTime() - PERIOD);
                    currentTimerValue = 0;
                    startTask();
                }
            }, Math.max(0, deltaTime));
        }
    }

    private boolean timerIsStopped() {
        return currentTimerValue == 0;
    }

    private boolean dateBeforeCurrentTimer(long taskTime) {
        return taskTime < currentTimerValue;
    }

    public void dispose() {
        timer.cancel();
    }
}
