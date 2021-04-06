package com.bear.brain.main;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.Texts;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.lib.S;

public class Caption extends Group {
    Label label;
    Container<Label> labelContainer;

    Image popup;

    public Caption() {
        popup = new Image(Resources.createPatch("popup_back.2x"));
        addActor(popup);
        label = new Label(null, Font.RUBIK14.black());
        label.setWrap(true);
        label.setAlignment(Align.center);
        labelContainer = new Container<>(label);
        addActor(labelContainer);
    }

    public void setCellsCount(int count) {
        label.setText(Texts.CLICK_ON_EACH_NUMBER.format(count));
        labelContainer.pack();
        labelContainer.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
    }

    public void setText(String text) {
        label.setText(text);
        labelContainer.pack();
        labelContainer.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
    }

    @Override
    protected void sizeChanged() {
        popup.setSize(getWidth() - S.u(24), getHeight() - S.u(24));
        popup.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
        labelContainer.prefWidth(popup.getWidth() - S.u(32));
        labelContainer.pack();
        labelContainer.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
    }
}
