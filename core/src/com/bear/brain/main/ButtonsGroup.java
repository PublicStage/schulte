package com.bear.brain.main;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.bear.brain.gear.HelpButton;
import com.bear.lib.S;

public class ButtonsGroup extends Group {
    HelpButton helpButton;
    SoundButton soundButton;

    public ButtonsGroup() {
        soundButton = new SoundButton();
        helpButton = new HelpButton();
        addActor(soundButton);
        addActor(helpButton);
    }

    @Override
    protected void sizeChanged() {
        helpButton.setPosition(getWidth() - helpButton.getWidth() - S.u(16), S.u(24));
        soundButton.setPosition(S.u(16), S.u(24));
    }
}
