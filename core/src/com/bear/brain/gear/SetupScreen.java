package com.bear.brain.gear;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.Brain;
import com.bear.brain.resources.Resources;
import com.bear.brain.resources.Sounds;
import com.bear.brain.widgets.Button;
import com.bear.lib.Fragment;
import com.bear.lib.S;

public class SetupScreen extends Fragment {
    Image back;
    Group popup;
    Image color;

    Button reset;

    public SetupScreen() {

        back = new Image(Resources.createPatch("black60"));
        addActor(back);
        //Container<Group> container = new Container<>();
        //container.fill();
        popup = new Group();
        color = new Image(Resources.createPatch("main_color"));
        addActor(back);

        popup.setSize(S.u(200), S.u(200));
        color.setSize(popup.getWidth(), popup.getHeight());
        popup.addActor(color);

        //container.setActor(popup);
        addActor(popup);

        reset = new Button("Reset");
        addActor(reset);

        popup.setTouchable(Touchable.enabled);

        reset.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                Brain.state.reset();
                Sounds.CLICK.play();
            }
        });

        back.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                Resources.menu.backScreen();
            }
        });

    }

    @Override
    protected void sizeChanged() {
        back.setSize(getWidth(), getHeight());
        popup.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
        reset.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
    }
}
