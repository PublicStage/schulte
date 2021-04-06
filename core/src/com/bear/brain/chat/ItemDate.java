package com.bear.brain.chat;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.brain.widgets.TimeLabel;
import com.bear.lib.ImageLayer;
import com.bear.lib.S;

public class ItemDate extends WidgetGroup {


    ImageLayer back;
    TimeLabel label;

    public ItemDate() {
        setTransform(false);
        back = new ImageLayer(Resources.createPatch("chat_item_back_dark.2x"));
        back.setLayerId(1);
        addActor(back);
        label = new TimeLabel(Font.RUBIK14.white());
        label.setLayerId(2);
        addActor(label);

    }

    public void setDate(long date) {
        label.setTime_dd_M(date);
        label.setPosition(getWidth() / 2 - label.getWidth() / 2, -S.u(4));
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);

        if (parent != null) {
            Group recycler = parent.getParent();
            setSize(recycler.getWidth(), S.u(19));
            back.setSize(label.getWidth() + S.u(16), getHeight());
            back.setPosition(getWidth() / 2 - back.getWidth() / 2, -S.u(4));
            label.setPosition(getWidth() / 2 - label.getWidth() / 2, -S.u(4));
        }
    }
}
