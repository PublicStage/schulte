package com.bear.brain.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.bear.brain.GameResult;
import com.bear.brain.logic.State;
import com.bear.brain.logic.StateListener;
import com.bear.lib.S;

public class Field extends Table implements StateListener {
    State state;
    DarkStart darkStart;

    Array<CellActor> cells = new Array<>();

    public Field(final State state) {
        this.state = state;


        darkStart = new DarkStart();
        addActor(darkStart);

        darkStart.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                state.startGame();

                if (isPositionOfFirstCell(localToStageCoordinates(new Vector2(x, y)))) {
                    state.press(1);
                }
            }
        });

        changeLevel(state.getSize());

        state.addStateListener(this);
    }

    boolean isPositionOfFirstCell(Vector2 pos) {
        for (CellActor c : cells) {
            if (Rectangle.tmp.set(c.getX(), c.getY(), c.getWidth(), c.getHeight()).contains(pos) && c.id == 1) {
                return true;
            }
        }
        return false;
    }

    private void addCells(int size) {

        float limitedCellTableWidth = Math.min(S.u(360), Gdx.graphics.getHeight() - S.u(200));
        int cellSize = (int) ((limitedCellTableWidth - S.u(12)) / size);

        cells.clear();
        for (int i = 0; i < size; i++) {
            add().expandX();
            for (int j = 0; j < size; j++) {
                CellActor cell = new CellActor(state, cellSize);
                add(cell).pad(S.u(2));
                state.addCell(cell);
                cells.add(cell);
            }
            add().expandX().row();
        }
    }

    @Override
    public void start() {
        //darkStart.setVisible(false);
        darkStart.addAction(Actions.moveTo(getWidth(), 0, 0.2f));
    }

    @Override
    public void finish(GameResult result) {
        //darkStart.setVisible(true);
        darkStart.sizeChanged();
        darkStart.addAction(Actions.moveTo(0, 0, 0.2f));
    }

    @Override
    public void press(int id) {

    }

    @Override
    public void changeLevel(int level) {
        cells.clear();
        clearChildren();
        add().colspan(level + 2).height(S.u(2)).width(S.u(360)).row();
        addCells(level);
        add().colspan(level + 2).height(S.u(2)).row();
        pack();

        darkStart.setSize(getWidth(), getHeight());
        addActor(darkStart);
    }
}
