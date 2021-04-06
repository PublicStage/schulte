package com.bear.brain.block;

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
import com.bear.brain.widgets.Button;
import com.bear.lib.Fragment;
import com.bear.lib.S;

public class NeedUpdate extends Fragment {
    Image dark;
    Image popup;
    Label label;
    Container<Label> labelContainer;
    Button button;

    public NeedUpdate() {
        dark = new Image(Resources.createPatch("black60"));
        addActor(dark);

        popup = new Image(Resources.createPatch("popup_back.2x"));
        addActor(popup);

        label = new Label(Texts.NEED_UPDATE.toString(), Font.RUBIK14.black());
        label.setWrap(true);
        label.setAlignment(Align.center);
        labelContainer = new Container<>(label);
        addActor(labelContainer);

        button = new Button(Texts.UPDATE.toString());
        addActor(button);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                Brain.goToMarket.execute();
            }
        });

    }

    @Override
    protected void sizeChanged() {
        dark.setSize(getWidth(), getHeight());
        labelContainer.prefWidth(getWidth() - S.u(64) - S.u(48));
        labelContainer.pack();
        labelContainer.setPosition(getWidth() / 2, getHeight() / 2 + S.u(24), Align.center);

        popup.setSize(getWidth() - S.u(48), labelContainer.getHeight() + S.u(64) + button.getHeight() + S.u(24));
        popup.setPosition(getWidth() / 2, getHeight() / 2, Align.center);

        button.setPosition(getWidth() / 2, popup.getY() + S.u(24), Align.bottom | Align.center);

    }
}
