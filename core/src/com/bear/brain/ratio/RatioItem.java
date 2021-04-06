package com.bear.brain.ratio;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.Brain;
import com.bear.brain.RItem;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.brain.widgets.TimeLabel;
import com.bear.lib.ImageLayer;
import com.bear.lib.LabelLayer;
import com.bear.lib.S;

public class RatioItem extends Group {
    LabelLayer place;
    LabelLayer name;
    TimeLabel score;
    ImageLayer back;
    ImageLayer backSelected;

    public RatioItem() {
        setSize(S.u(360), S.u(50));
        back = new ImageLayer(Resources.createPatch("item_back_grad"));
        back.setLayerId(1);
        backSelected = new ImageLayer(Resources.createPatch("back_green"));
        backSelected.setLayerId(1);
        back.setSize(getWidth(), getHeight() - S.u(2));
        backSelected.setSize(getWidth(), getHeight() - S.u(2));
        addActor(back);
        addActor(backSelected);
        back.setPosition(0, S.u(1));

        place = new LabelLayer("1", Font.RUBIK14.black());
        place.setLayerId(2);
        name = new LabelLayer("Player name", Font.RUBIK14.black());
        name.setLayerId(2);
        score = new TimeLabel(Font.RUBIK14.black());
        score.setLayerId(2);

        addActor(place);
        addActor(name);
        addActor(score);

        place.setPosition(S.u(20), getHeight() / 2, Align.center);
        name.setPosition(S.u(40), getHeight() / 2 - name.getHeight() / 2);
        score.setPosition(getWidth() - score.getWidth() - S.u(16), getHeight() / 2 - score.getHeight() / 2);
    }

    @Override
    public float getWidth() {
        return S.u(360);
    }

    @Override
    public float getHeight() {
        return S.u(48);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(S.u(360), S.u(48));
    }

    public void setData(int position, RItem data) {
        if (data != null) {

            backSelected.setVisible(data.isOwner());

            place.setText(position + 1);
            place.pack();

            if (data.isOwner()) {
                name.setText(Brain.player.getName());
            } else {
                if (data.getName() != null) {
                    name.setText(data.getName().substring(0, Math.min(20, data.getName().length())));
                } else {
                    name.setText("null");
                }
            }
            name.pack();
            score.setTime_ss_SS(data.getScore());
            score.pack();

            place.setPosition(S.u(20), getHeight() / 2, Align.center);
            name.setPosition(S.u(48), getHeight() / 2 - name.getHeight() / 2);
            score.setPosition(getWidth() - score.getWidth() - S.u(16), getHeight() / 2 - score.getHeight() / 2);
        } else {
            backSelected.setVisible(false);
            place.setText(position + 1);
            place.pack();
            name.setText(null);
            score.setText(null);
        }
    }
}
