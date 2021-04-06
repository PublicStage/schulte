package com.bear.brain.menu;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.bear.brain.Brain;
import com.bear.brain.GameResult;
import com.bear.brain.logic.StateListener;
import com.bear.brain.resources.Resources;
import com.bear.brain.resources.Sounds;
import com.bear.lib.S;

public class TopHeader extends Group implements StateListener {
    Image topBar;

    MenuButton[] array = new MenuButton[3];

    String[] captions = {"3x3", "4x4", "5x5"};

    public TopHeader(final int selected) {
        setTransform(false);
        setTouchable(Touchable.childrenOnly);
        setSize(S.u(360), S.u(76));

        array[0] = new MenuButton();
        array[1] = new RatingMenuButton("rating_ico_light.2x", "rating_ico.2x");
        array[2] = new MenuButton();

        for (int i = 0; i < 3; i++) {
            addActor(array[i]);
            array[i].setPosition(S.u(120 * i), S.u(20));
        }

        array[0].addLabel("");
        array[2].addIco("chat_ico_light.2x", "chat_ico.2x");

        array[selected].setSelected(true);

        topBar = new Image(Resources.createPatch("bar_up.2x"));
        topBar.setTouchable(Touchable.disabled);
        topBar.setWidth(getWidth());
        addActor(topBar);
        array[selected].setZIndex(topBar.getZIndex() + 1);

        changeLevel(Brain.state.getSize());

        array[0].addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (selected != 0) {
                    Sounds.CLICK.play(0.3f);
                    Resources.menu.setGame();
                }
            }
        });

        array[1].addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (selected != 1) {
                    Sounds.CLICK.play(0.3f);
                    Resources.menu.setRating();
                }
            }
        });

        array[2].addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (selected != 2) {
                    Sounds.CLICK.play(0.3f);
                    Resources.menu.setChat();
                }
            }
        });
    }


    @Override
    public void start() {

    }

    @Override
    public void finish(GameResult result) {

    }

    @Override
    public void press(int id) {

    }

    @Override
    public void changeLevel(int level) {
        array[0].setText(captions[level - 3]);
    }

    public void dispose() {
        for (int i = 0; i < 3; i++) {
            array[i].dispose();
        }
    }
}
