package com.bear.brain;

import android.app.Activity;

import com.bear.brain.utils.IntentMessage;

public class AndroidIntentMessage extends IntentMessage {
    public AndroidIntentMessage(Activity context) {
        if (context.getIntent().hasExtra(AndroidAlarmNotification.MESSAGE)) {
            message = context.getIntent().getStringExtra(AndroidAlarmNotification.MESSAGE);
        }
    }
}