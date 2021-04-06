package com.bear.brain;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bear.lib.SoftKeyboard;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        Brain game = new Brain();
        Brain.goToMarket = new AndroidGoToMarket(this);
        game.alarmSender = new AndroidAlarmSender(this);
        game.intentMessage = new AndroidIntentMessage(this);
        initialize(game, config);
        SoftKeyboard.setupApplicationAsKeyboardResizeListener(this, game);
    }
}
