package com.bear.brain;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.bear.brain.utils.AlarmSender;

public class AndroidAlarmSender extends AlarmSender {
    Context context;
    int MAX_INTENT_COUNT = 5;
    int intentCount = 0;

    public AndroidAlarmSender(Context context) {
        this.context = context;
    }

    @Override
    public void addAlarm(long time, String title, String message) {
        if (intentCount < MAX_INTENT_COUNT) {
            Intent intent = new Intent(context, AndroidAlarmNotification.class);
            intent.putExtra(AndroidAlarmNotification.TITLE, title);
            intent.putExtra(AndroidAlarmNotification.MESSAGE, message);
            PendingIntent operation = PendingIntent.getBroadcast(context, intentCount++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC, time, operation);
        }
    }

    @Override
    public void removeAlarms() {
        Intent intent = new Intent(context, AndroidAlarmNotification.class);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (int i = 0; i < MAX_INTENT_COUNT; i++) {
            am.cancel(PendingIntent.getBroadcast(context, i, intent, 0));
        }
        intentCount = 0;
    }

    @Override
    public void removeNotification() {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }
}
