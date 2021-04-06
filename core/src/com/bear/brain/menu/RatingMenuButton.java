package com.bear.brain.menu;

import com.bear.brain.Brain;
import com.bear.brain.events.ChangeLevel;
import com.bear.brain.events.ChangePlace;
import com.bear.lib.S;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class RatingMenuButton extends MenuButton {

    public RatingMenuButton(String open, String close) {
        addIco(open, close);
        EventBus.getDefault().register(this);
        addLabel(null);
        onMessageEvent(new ChangeLevel(Brain.state.getSize()));
    }

    @Override
    protected void setIconPosition() {
        if (Brain.player.haveUsersOnCurrentLevel()) {
            setIcoLeftPad(S.u(24));
        } else {
            super.setIconPosition();
        }
    }

    private void setIcoLeftPad(int pad) {
        ico_open.setPosition(pad, getHeight() / 2 - ico_open.getHeight() / 2 + S.u(2));
        ico_close.setPosition(pad, getHeight() / 2 - ico_close.getHeight() / 2 + S.u(1));
    }

    @Override
    protected void setLabelPosition() {
        label.setPosition(S.u(78) - label.getWidth() / 2, getHeight() / 2 - label.getHeight() / 2);
    }

    private void setPlaceOfCurrentLevel() {
        if (Brain.player.haveUsersOnCurrentLevel()) {
            if (Brain.player.haveResultOnCurrentLevel()) {
                setText(Brain.player.getPlaceOnCurrentLevel() + " / " + Brain.player.getUsersCountOnCurrentLevel());
            } else {
                setText("... / " + Brain.player.getUsersCountOnCurrentLevel());
            }
            setIcoLeftPad(label.getText().length > 8 ? S.u(16) : S.u(24));
        } else {
            setText(null);
            setIconPosition();
        }

    }

    @Subscribe
    public void onMessageEvent(ChangeLevel event) {
        setPlaceOfCurrentLevel();
    }


    @Subscribe
    public void onMessageEvent(ChangePlace event) {
        setPlaceOfCurrentLevel();
    }


    @Override
    public void dispose() {
        EventBus.getDefault().unregister(this);
        super.dispose();
    }
}
