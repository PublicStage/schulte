package com.bear.brain.diagram3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.brain.widgets.TimeLabel;
import com.bear.lib.ImageLayer;
import com.bear.lib.S;

public class Item extends Group {
    ImageLayer diagram;
    ImageLayer diagramBest;
    TimeLabel label;
    TimeLabel date;
    ImageLayer star;

    boolean tracked = false;
    float fixedItemY;

    FixedItemListener listener;

    public Item() {
        setTransform(false);
        diagram = new ImageLayer(Resources.createPatch("diagram.2x"));
        diagram.setLayerId(1);
        diagramBest = new ImageLayer(Resources.createPatch("black7"));
        diagramBest.setLayerId(1);
        addActor(diagram);
        addActor(diagramBest);
        label = new TimeLabel(Font.RUBIK14.white());
        label.setLayerId(2);
        addActor(label);
        date = new TimeLabel(Font.RUBIK10.black());
        date.setLayerId(3);
        addActor(date);
        star = Resources.createImageLayer("rating_ico_red.2x");
        star.setLayerId(1);
        addActor(star);
        setSize(Gdx.graphics.getWidth(), S.u(24));
    }

    Vector2 tmp = new Vector2();
    float lastTrackedY = 0;

    public void setFixedItemY(float fixedItemY) {
        this.fixedItemY = fixedItemY;
    }

    public void setFixedItemListener(FixedItemListener listener) {
        if (this.listener == null) {
            this.listener = listener;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        track();
    }

    private void track() {
        if (tracked) {
            localToStageCoordinates(tmp.set(0, 0));
            if (tmp.y != lastTrackedY) {
                lastTrackedY = tmp.y;

                if (tmp.y > fixedItemY) {
                    listener.setItemVisible(true);
                } else {
                    listener.setItemVisible(false);
                }

            }
        }
    }

    public void setTime(long time) {
        label.setTime_ss_SS(time);
    }

    public void setDate(long value) {
        date.setTime_hh_mm(value);
    }

    public void setBest(boolean value) {
        setTracked(value);
        star.setVisible(value);
    }

    public void setTracked(boolean tracked) {
        this.tracked = tracked;
        lastTrackedY = 0;
    }

    public void setLength(float value, float bestValue) {
        //back.setWidth((getWidth() - S.u(48)) * value);
        diagram.setWidth(S.u(304) * value);
        diagramBest.setWidth(S.u(304) * bestValue);
        sizeChanged();
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, S.u(24));
    }

    @Override
    protected void sizeChanged() {
        diagram.setHeight(S.u(18));
        diagramBest.setHeight(S.u(18));
        diagram.setPosition(S.u(22), getHeight() / 2 - diagram.getHeight() / 2);
        diagramBest.setPosition(S.u(22), getHeight() / 2 - diagramBest.getHeight() / 2);

        label.setPosition(S.u(22) + diagram.getWidth() / 2, getHeight() / 2, Align.center);
        date.setPosition(diagram.getX() + diagram.getWidth() + S.u(2), diagram.getY() - S.u(3));

        star.setPosition(diagram.getX() / 2, getHeight() / 2, Align.center);
    }
}
