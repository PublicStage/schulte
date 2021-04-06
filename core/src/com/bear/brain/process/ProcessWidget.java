package com.bear.brain.process;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.Brain;
import com.bear.brain.GameResult;
import com.bear.brain.logic.MaxListener;
import com.bear.brain.logic.StateListener;
import com.bear.brain.resources.Font;
import com.bear.brain.widgets.TimeLabel;
import com.bear.lib.S;

public class ProcessWidget extends Group implements StateListener, MaxListener {
    Label current;
    TimeTab timeTab;

    TimeLabel max;
    TimeLabel mid;
    TimeLabel min;

    TimeLabel timer;

    public ProcessWidget() {
        timeTab = new TimeTab();
        timeTab.setColumnsCount(Brain.state.getCellsCount());
        addActor(timeTab);
        setSize(timeTab.getWidth() + S.u(30), timeTab.getHeight() + S.u(50));
        timeTab.setPosition(getWidth(), getHeight() - S.u(32), Align.topRight);

        max = new TimeLabel(Font.RUBIK14.white());
        mid = new TimeLabel(Font.RUBIK14.white());
        min = new TimeLabel(Font.RUBIK14.white());

        timer = new TimeLabel(Font.RUBIK14.white()) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setTime_ss_SS(Brain.state.timerValue());
                setX(ProcessWidget.this.getWidth() / 2, Align.center);
            }
        };
        timer.setStyle(Font.RUBIK48.white());
        timer.setAlignment(Align.center);
        timer.setTime_ss_SS(12345);
        timer.setPosition(getWidth() / 2, getHeight(), Align.top | Align.center);

        addActor(max);
        addActor(mid);
        addActor(min);

        addActor(timer);

        current = new Label("1", Font.RUBIK14.white());
        addActor(current);
        current.setPosition(S.u(31), 0);

        Brain.state.addStateListener(this);
        Brain.state.pressTimer.addMaxListener(this);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Brain.state.act();
    }

    @Override
    public void start() {
        press(0);
    }

    @Override
    public void finish(GameResult result) {
        current.setVisible(false);
        timeTab.setColumnsHeights();
    }

    @Override
    public void press(int id) {
        if (id < Brain.state.getCellsCount()) {
            current.setVisible(true);
            current.setText(id + 1);
            current.pack();

            float p = (timeTab.getWidth() - S.u(4)) / (3 * Brain.state.getCellsCount() - 1);
            float width = p * 3;

            current.setPosition(timeTab.getX() + S.u(2) + id * width + p - current.getWidth() / 2, 0);
        } else {
            current.setVisible(false);
        }
    }

    @Override
    public void changeLevel(int level) {
        timeTab.setColumnsCount(Brain.state.getCellsCount());
    }

    @Override
    public void onMaxTimeChange(long maxTime) {
        max.setTime_ss_S(maxTime);
        mid.setTime_ss_S(maxTime / 2);
        min.setTime_ss_S(0);

        max.setPosition(timeTab.getX() - S.u(8) - max.getWidth(), timeTab.getY() + timeTab.getHeight() - max.getHeight() / 2);
        mid.setPosition(timeTab.getX() - S.u(8) - mid.getWidth(), timeTab.getY() + timeTab.getHeight() / 2 - mid.getHeight() / 2);
        min.setPosition(timeTab.getX() - S.u(8) - min.getWidth(), timeTab.getY() - min.getHeight() / 2);
    }
}
