package com.bear.brain;

import com.bear.brain.net.SendListener;

public class OkSendListener implements SendListener {
    boolean ok = false;

    public boolean isOk() {
        return ok;
    }

    @Override
    public void ok() {
        ok = true;
    }

    @Override
    public void cancel() {
        ok = false;
    }
}
