package com.bear.brain.ratio.youratio;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.resources.Resources;
import com.bear.lib.S;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class Progress extends Group {
    Image image;

    public Progress() {
        setTransform(false);
        setSize(S.u(20), S.u(20));
        image = Resources.createImage("progress_2x");
        image.setOrigin(Align.center);
        addActor(image);
        setVisible(true);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        image.clearActions();
        if (visible) {
            image.addAction(forever(sequence(delay(0.1f), Actions.rotateBy(-45))));
        }
    }
}
