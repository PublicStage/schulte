package com.bear.brain.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.lib.S;

public class MenuButton extends Group {
    Image open;
    Image close;
    Label label;
    Image ico_open;
    Image ico_close;

    public MenuButton() {
        setTransform(false);
        open = Resources.createImage("tab_open.2x");
        close = Resources.createImage("tab_close.2x");
        addActor(open);
        addActor(close);
        setSize(open.getWidth(), open.getHeight());
        setSelected(false);
    }

    public void setSelected(boolean selected) {
        open.setVisible(selected);
        close.setVisible(!selected);
        if (ico_open != null) ico_open.setVisible(selected);
        if (ico_close != null) ico_close.setVisible(!selected);

    }

    public void addLabel(String caption) {
        label = new Label(null, Font.RUBIK14.white());
        label.setColor(Color.valueOf("513E17"));
        addActor(label);
        setText(caption);
        label.setTouchable(Touchable.disabled);
    }

    public void addIco(String open, String close) {
        ico_open = Resources.createImage(open);
        ico_close = Resources.createImage(close);
        addActor(ico_open);
        addActor(ico_close);

        setIconPosition();

        setSelected(false);
    }

    protected void setIconPosition() {
        ico_open.setPosition(getWidth() / 2 - ico_open.getHeight() / 2, getHeight() / 2 - ico_open.getHeight() / 2 + S.u(2));
        ico_close.setPosition(getWidth() / 2 - ico_close.getHeight() / 2, getHeight() / 2 - ico_close.getHeight() / 2 + S.u(1));
    }

    public void setText(String caption) {
        label.setText(caption);
        label.pack();
        setLabelPosition();
    }

    protected void setLabelPosition() {
        label.setPosition(getWidth() / 2 - label.getWidth() / 2, getHeight() / 2 - label.getHeight() / 2);
    }

    public void dispose() {

    }


}
