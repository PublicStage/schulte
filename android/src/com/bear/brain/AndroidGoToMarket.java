package com.bear.brain;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.badlogic.gdx.Gdx;
import com.bear.brain.utils.GoToMarket;

public class AndroidGoToMarket extends GoToMarket {
    Context context;

    public AndroidGoToMarket(Context context) {
        this.context = context;
    }

    @Override
    public void execute() {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.mobal.shulte"));
        try {
            context.startActivity(webIntent);
        } catch (ActivityNotFoundException anfex) {
            Gdx.app.log("Exception", anfex.getMessage());
        }

    }
}
