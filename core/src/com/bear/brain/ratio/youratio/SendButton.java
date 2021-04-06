package com.bear.brain.ratio.youratio;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.resources.Resources;
import com.bear.lib.S;

public class SendButton extends Group {
    public SendButton() {
        setTransform(false);
        setSize(S.u(64), S.u(32));
        Image image = Resources.createImage("send_2x");
        image.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
        addActor(image);
    }
}