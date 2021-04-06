package com.bear.brain.utils;

import com.bear.brain.Texts;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlarmSender {


    public void addAlarm() {
        List<String> pushMessages = Arrays.asList(Texts.pushMessages);
        Collections.shuffle(pushMessages);

        addAlarm(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1), Texts.PUSH_TITLE.toString(), pushMessages.get(0));
        addAlarm(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(3), Texts.PUSH_TITLE.toString(), pushMessages.get(1));
        addAlarm(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(5), Texts.PUSH_TITLE.toString(), pushMessages.get(2));
        addAlarm(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7), Texts.PUSH_TITLE.toString(), pushMessages.get(3));
    }

    public void addAlarm(long time, String title, String message) {
    }

    public void removeAlarms() {
    }

    public void removeNotification() {
    }
}