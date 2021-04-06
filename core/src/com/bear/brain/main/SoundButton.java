package com.bear.brain.main;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.bear.brain.resources.Resources;
import com.bear.brain.resources.Sounds;

public class SoundButton extends Group {
    Image imageOn;
    Image imageOff;
    boolean state;

    public SoundButton() {
        imageOn = Resources.createImage("sound_on.2x");
        imageOff = Resources.createImage("sound_off.2x");
        addActor(imageOn);
        addActor(imageOff);
        setSize(imageOn.getWidth(), imageOn.getHeight());
        setSize(imageOff.getWidth(), imageOff.getHeight());
        imageOff.setVisible(false);

        setState(Sounds.ON);
        
        addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                state = !state;
                setState(state);
                Sounds.ON = state;
            }
        });
    }

    public void setState(boolean value) {
        imageOn.setVisible(value);
        imageOff.setVisible(!value);
    }
}
