package com.bear.brain.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.lib.S;

public class Button extends Group {
    Image back;
    Image pressed;
    Label label;

    public Button(CharSequence str) {
        setSize(S.u(120), S.u(32));
        back = new Image(Resources.createPatch("button"));
        pressed = new Image(Resources.createPatch("button_pressed"));
        back.setSize(getWidth() + 8f * S.PIXEL_PER_UNIT, getHeight() + 8f * S.PIXEL_PER_UNIT);
        pressed.setSize(getWidth(), getHeight());
        back.setPosition(-4f * S.PIXEL_PER_UNIT, -5f * S.PIXEL_PER_UNIT);
        addActor(back);
        addActor(pressed);
        pressed.setVisible(false);

        label = new Label(str, Font.RUBIK14.white());
        label.setColor(Color.BLACK);
        label.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
        addActor(label);

        addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                press(true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                press(false);
            }
        });
    }

    public void press(boolean value) {
        back.setVisible(!value);
        pressed.setVisible(value);
        //label.setColor(value ? Color.DARK_GRAY : Color.BLACK);

        label.setPosition(getWidth() / 2, getHeight() / 2 - (value ? 1 : 0), Align.center);
    }
}