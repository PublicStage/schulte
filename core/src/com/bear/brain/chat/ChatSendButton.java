package com.bear.brain.chat;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.resources.Resources;
import com.bear.lib.S;

public class ChatSendButton extends Group {
    Image back;

    public ChatSendButton() {
        setTransform(false);
        setSize(S.u(64), S.u(38));

        back = new Image(Resources.createPatch("chat_item_back.2x"));
        back.setSize(getWidth(), getHeight());
        addActor(back);

        Image image = Resources.createImage("send_2x");
        image.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
        addActor(image);

        addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                back.setVisible(false);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                back.setVisible(true);
            }
        });
    }
}