package com.bear.brain.diagram3;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.bear.brain.Brain;
import com.bear.brain.GameResult;
import com.bear.brain.logic.StateListener;
import com.bear.lib.RecyclerDX;
import com.bear.lib.S;

public class Diagram3 extends Group implements StateListener, FixedItemListener {
    RecyclerDX rx;
    FixedItem fixed;

    ItemAdapter adapter;

    public Diagram3() {
        adapter = new ItemAdapter(this);
        rx = new RecyclerDX(adapter);
        rx.setCompact(true);
        addActor(rx);
        fixed = new FixedItem();
        addActor(fixed);

        Brain.state.addStateListener(this);
    }

    @Override
    protected void sizeChanged() {
        fixed.setPosition(0, getHeight() - fixed.getHeight());
        adapter.setFixedItemY(localToStageCoordinates(new Vector2(0, fixed.getY())).y + S.u(4));

        rx.setSize(getWidth(), getHeight());
        rx.setPosition((int) (getWidth() / 2 - rx.getWidth() / 2), (int) (getHeight() / 2 - rx.getHeight() / 2));
        setItemVisible(true);
        rx.scrollToBack();
        rx.act(0);
    }

    @Override
    public void start() {

    }

    @Override
    public void finish(GameResult result) {
        fixed.loadValues();
        sizeChanged();
    }

    @Override
    public void press(int id) {

    }

    @Override
    public void changeLevel(int level) {
        fixed.loadValues();
        sizeChanged();
    }

    @Override
    public void setItemVisible(boolean value) {
        fixed.setVisible(value);
    }
}
