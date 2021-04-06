package com.bear.brain.gear;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.bear.brain.Texts;
import com.bear.brain.help.PopupFragment;
import com.bear.brain.resources.Resources;

public class HelpButton extends Group {
    public HelpButton() {
        Image image = Resources.createImage("help.2x");
        addActor(image);
        setSize(image.getWidth(), image.getHeight());

        addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                Resources.menu.addScreen(new PopupFragment(Texts.HELP.toString()));
            }
        });
    }
}
