package com.bear.brain.main;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.Brain;
import com.bear.brain.resources.Resources;
import com.bear.lib.S;

public class DarkStart extends Group {
    Image back;
    HandAnimation handAnimation;

    public DarkStart() {
        setTouchable(Touchable.enabled);
        back = new Image(Resources.createPatch("black80"));
        addActor(back);
        handAnimation = new HandAnimation();
        addActor(handAnimation);
    }

    @Override
    protected void sizeChanged() {
        back.setSize(getWidth(), getHeight());
        handAnimation.setPosition(getWidth() / 2, Brain.state.getResultsCount() == 0 ? getHeight() / 2 : S.u(139), Align.center);
    }
}
