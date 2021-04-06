package com.bear.brain.widgets;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.bear.brain.Save;
import com.bear.brain.logic.State;
import com.bear.brain.resources.Sounds;
import com.bear.lib.S;

public class Levels extends Table {
    public Levels(final State state) {
        setHeight(S.u(36));
        for (int i = State.MIN_LEVEL; i <= State.MAX_LEVEL; i++) {
            LevelButton button = new LevelButton(i);
            state.addStateListener(button);
            button.changeLevel(state.getSize());
            add(button).width(S.u(72)).padLeft(S.u(2)).padRight(S.u(2)).height(S.u(36));

            final int finalI = i;
            button.addListener(new ActorGestureListener() {
                @Override
                public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    state.selectLevel(finalI);
                    Save.saveLevel(finalI);
                    Sounds.CLICK.play(0.3f);
                }
            });
        }
    }
}
