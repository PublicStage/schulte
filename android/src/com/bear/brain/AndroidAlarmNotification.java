package com.bear.brain;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.graphics.Color.argb;

public class AndroidAlarmNotification extends BroadcastReceiver {
    public static String TITLE = "TITLE";
    public static String MESSAGE = "MESSAGE";

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra(TITLE);
        String message = intent.getStringExtra(MESSAGE);

        //Интент для активити, которую мы хотим запускать при нажатии на уведомление
        Intent intentTL = new Intent(context, AndroidLauncher.class);
        intentTL.putExtra(MESSAGE, message);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentTL, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Notification.Builder nb = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ico4)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                nb.setColor(argb(1, 238, 214, 140));
            }


            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
//                notificationManager.createNotificationChannel(channel);
//            }

            notificationManager.notify(0, nb.build());

        }
    }
}