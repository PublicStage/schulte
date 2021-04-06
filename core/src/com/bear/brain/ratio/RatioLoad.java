package com.bear.brain.ratio;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.resources.Font;
import com.bear.lib.S;

public class RatioLoad extends Group {
    public RatioLoad() {
        setSize(S.u(344), S.u(24));
        Label label = new Label("loading...", Font.RUBIK14.black());
        addActor(label);
        label.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
    }

    @Override
    public float getWidth() {
        return S.u(344);
    }

    @Override
    public float getHeight() {
        return S.u(24);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(S.u(344), S.u(24));
    }
}