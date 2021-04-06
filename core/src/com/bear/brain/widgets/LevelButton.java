package com.bear.brain.widgets;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.bear.brain.GameResult;
import com.bear.brain.logic.StateListener;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.lib.S;

public class LevelButton extends Group implements StateListener {

    Image back;
    Image selected;
    Label caption;

    int level;

    public LevelButton(int level) {
        this.level = level;
        setTransform(false);
        back = Resources.createImage("tab_small.2x");
        selected = Resources.createImage("tab_small_short.2x");
        //back = new Image(Resources.createPatch("tab_small.2x"));
        //selected = new Image(Resources.createPatch("tab_small_short.2x"));
        caption = new Label(level + "x" + level, Font.RUBIK14.black());
        caption.setTouchable(Touchable.disabled);
        back.setTouchable(Touchable.disabled);
        selected.setTouchable(Touchable.disabled);

        addActor(back);
        addActor(selected);
        addActor(caption);

        selected.setVisible(false);
    }

    @Override
    protected void sizeChanged() {
        //back.setSize(getWidth(), getHeight());
        //selected.setSize(getWidth(), getHeight());
        caption.setPosition(getWidth() / 2 - caption.getWidth() / 2, selected.isVisible() ? S.u(2) : S.u(12));
    }

    @Override
    public void start() {

    }

    @Override
    public void finish(GameResult result) {

    }

    @Override
    public void press(int id) {

    }

    @Override
    public void changeLevel(int level) {
        selected.setVisible(this.level == level);
        back.setVisible(this.level != level);
        caption.setPosition(getWidth() / 2 - caption.getWidth() / 2, selected.isVisible() ? S.u(2) : S.u(12));
    }
}
