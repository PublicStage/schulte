package com.bear.brain.help;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.Brain;
import com.bear.brain.Texts;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.brain.resources.Sounds;
import com.bear.brain.widgets.Button;
import com.bear.lib.Fragment;
import com.bear.lib.S;

public class HelpFragment extends Fragment {
    Image back;
    Image popup;
    Label label;
    Container<Label> labelContainer;
    Button reset;

    public HelpFragment() {
        back = new Image(Resources.createPatch("black60"));
        addActor(back);
        popup = new Image(Resources.createPatch("popup_back.2x"));
        addActor(popup);

        label = new Label(Texts.HELP.toString(), Font.RUBIK14.black());
        label.setWrap(true);
        label.setAlignment(Align.left);
        labelContainer = new Container<>(label);
        addActor(labelContainer);

        reset = new Button("reset");
        addActor(reset);
        reset.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                Brain.state.reset();
                Sounds.CLICK.play();
            }
        });


        addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Resources.menu.backScreen();
            }
        });
    }

    @Override
    protected void sizeChanged() {
        back.setSize(getWidth(), getHeight());
        labelContainer.prefWidth(getWidth() - S.u(64) - S.u(48));
        labelContainer.pack();
        labelContainer.setPosition(getWidth() / 2, getHeight() / 2 + S.u(24), Align.center);

        popup.setSize(getWidth() - S.u(48), labelContainer.getHeight() + S.u(64) + reset.getHeight() + S.u(24));
        popup.setPosition(getWidth() / 2, getHeight() / 2, Align.center);

        reset.setPosition(getWidth() / 2, popup.getY() + S.u(24), Align.bottom | Align.center);
    }
}
