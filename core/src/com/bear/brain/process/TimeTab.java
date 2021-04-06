package com.bear.brain.process;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.bear.brain.Brain;
import com.bear.brain.logic.MaxListener;
import com.bear.brain.logic.TicListener;
import com.bear.brain.resources.Resources;
import com.bear.lib.S;

public class TimeTab extends Group implements MaxListener, TicListener {

    Array<Image> array = new Array<>();

    int count;
    float minHeight = S.u(2);
    long maxTime;

    public TimeTab() {

        float minHeight = S.u(50);
        float maxHeight = S.u(97);

        float tabHeight = Math.max(minHeight, Math.min(maxHeight, Gdx.graphics.getHeight() - Gdx.graphics.getWidth() - S.u(145)));
        setSize(S.u(300), tabHeight);

        Brain.state.pressTimer.addMaxListener(this);
        Brain.state.pressTimer.addTicListener(this);
    }

    private void setLines(int count) {
        for (int i = 0; i < 5; i++) {
            Image line = new Image(Resources.createPatch("white"));
            line.setSize(getWidth(), S.u(1));
            addActor(line);
            line.setPosition(0, (int) (getHeight() / 4f * i));
        }

        for (int i = 0; i < count; i++) {
            Image line = new Image(Resources.createPatch("white"));
            line.setSize(S.u(1), getHeight());
            addActor(line);
            line.setPosition((int) (S.u(4) + (getWidth() - S.u(8)) / (float) (count - 1) * i), 0);
        }
    }

    public void setColumnsCount(int count) {
        this.count = count;
        clearChildren();
        setLines(count == 16 ? 6 : 5);

        float p = (getWidth() - S.u(4)) / (3 * count - 1);
        float width = p * 3;

        array.clear();
        for (int i = 0; i < count; i++) {
            Image green = new Image(Resources.createPatch("green"));
            //green.setSize(p * 2f, getHeight() / 4 + MathUtils.random(getHeight() / 2));
            green.setSize(p * 2f, S.u(4));
            addActor(green);
            green.setPosition(S.u(2) + i * width, S.u(2));
            array.add(green);
        }
        onMaxTimeChange(maxTime);
    }

    @Override
    public void onMaxTimeChange(long maxTime) {
        this.maxTime = maxTime;
        setColumnsHeights();
    }

    public void setColumnsHeights() {
        for (int i = 0; i < count; ++i) {
            array.get(i).setHeight(Math.max(minHeight, getHeight() * Brain.state.pressTimer.getTime(i) / maxTime));
        }
    }

    @Override
    public void tic() {
        int current = Brain.state.getNext();
        long time = Brain.state.pressTimer.timerValue();
        array.get(current - 1).setHeight(getHeight() * time / maxTime);
    }
}
