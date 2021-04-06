package com.bear.brain.main;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.logic.Cell;
import com.bear.brain.logic.State;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;

public class CellActor extends Group implements Cell {
    Image back;
    Image selected;
    Label label;
    int id;
    State state;

    public CellActor(final State state, int size) {

        this.state = state;
        setSize(size, size);
        back = new Image(Resources.createPatch("cell.2x"));
        selected = new Image(Resources.createPatch("cell_green.2x"));
        back.setSize(getWidth(), getHeight());
        selected.setSize(getWidth(), getHeight());
        addActor(back);
        addActor(selected);
        label = new Label(null, Font.RUBIK48.black());
        addActor(label);

        addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                state.press(id);
            }
        });
    }

    public void setId(int id) {
        this.id = id;
        setText(String.valueOf(id));
        back.setVisible(true);
        selected.setVisible(false);
    }

    @Override
    public void setBack(boolean visible) {
        selected.setVisible(!visible);
    }

    public void setText(String text) {
        label.setText(text);
        label.pack();
        label.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
    }
}
