package com.bear.brain.chat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.bear.brain.ChatMessage;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.brain.widgets.TimeLabel;
import com.bear.lib.ImageLayer;
import com.bear.lib.LabelLayer;
import com.bear.lib.S;

public class Item extends WidgetGroup {
    static GlyphLayout prefSizeLayout = new GlyphLayout();
    ImageLayer back;
    LabelLayer name;
    LabelLayer message;
    Container<LabelLayer> messageContainer;
    LabelLayer place;
    TimeLabel time;
    ImageLayer ratingIco;

    float pad = S.u(8);

    public Item() {
        setTransform(false);
        back = new ImageLayer(Resources.createPatch("chat_item_back.2x"));
        back.setLayerId(1);

        name = new LabelLayer("Name", Font.RUBIK14.white());
        name.setLayerId(2);
        name.setColor(Color.valueOf("408332"));

        message = new LabelLayer("Message", Font.RUBIK14.black());
        message.setLayerId(2);
        message.setWrap(true);
        messageContainer = new Container<>(message);
        messageContainer.setTransform(false);

        place = new LabelLayer(null, Font.RUBIK10.white());
        place.setLayerId(3);
        place.setColor(Color.valueOf("408332"));

        ratingIco = Resources.createImageLayer("rating_ico_small.2x");
        ratingIco.setLayerId(1);

        time = new TimeLabel(Font.RUBIK10.white());
        time.setLayerId(3);
        time.setColor(Color.valueOf("716643"));

        time.setTime_hh_mm(System.currentTimeMillis());

        addActor(back);
        addActor(name);
        addActor(messageContainer);
        addActor(place);
        addActor(ratingIco);
        addActor(time);
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);

        if (parent != null) {
            Group recycler = parent.getParent();
            setSizes(recycler.getWidth());
        }
    }

    public void setSizes(float width) {

        float maxMessageWidth = width - 4 * pad - S.u(2); // -2unit потому что label при wrap вылазит одним символом за границу

        prefSizeLayout.setText(Font.RUBIK14.font(), message.getText());
        boolean timeMoveDown = prefSizeLayout.width > maxMessageWidth - time.getWidth(); // если message и time не помещаются на одной строке

        messageContainer.prefWidth(maxMessageWidth);
        messageContainer.pack();

        float height = pad * 1.5f + name.getHeight() + messageContainer.getHeight();

        if (timeMoveDown) height += time.getHeight();

        setSize((int) width, (int) height);

        name.setPosition(2 * pad, getHeight() - pad - name.getHeight());
        messageContainer.setPosition(2 * pad, name.getY() - messageContainer.getHeight());

        place.setPosition(getWidth() - 2 * pad - place.getWidth(), getHeight() - pad * 1.5f - place.getHeight());
        time.setPosition(getWidth() - 2 * pad - time.getWidth(), S.u(2));

        ratingIco.setPosition(place.getX() - S.u(4) - ratingIco.getWidth(), place.getY() + place.getHeight() / 2 - ratingIco.getHeight() / 2);

        back.setSize(getWidth() - 2 * pad, getHeight() - pad);
        back.setPosition(pad, 0);
    }

    public void setMessage(ChatMessage chatMessage) {
        name.setText(chatMessage.getName());
        name.pack();
        message.setText(chatMessage.getMessage());
        time.setTime_hh_mm(chatMessage.getTime());
        if (chatMessage.hasPlace()) {
            place.setVisible(true);
            ratingIco.setVisible(true);
            place.setText(chatMessage.getPlace());
            place.pack();
        } else {
            place.setVisible(false);
            ratingIco.setVisible(false);
        }
    }

}
