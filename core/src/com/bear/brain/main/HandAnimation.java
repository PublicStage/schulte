package com.bear.brain.main;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.resources.Resources;
import com.bear.lib.S;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.hide;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeTo;

public class HandAnimation extends Group {

    public HandAnimation() {

        setSize(S.u(138), S.u(100));

        Image hand = Resources.createImage("hand.2x");
        hand.setOrigin(S.u(31), S.u(89));

        hand.setPosition(S.u(38), 0);

        addActor(hand);

        final Image[] images = new Image[3];
        for (int i = 0; i < images.length; i++) {
            images[i] = new Image(Resources.createPatch("white5.2x"));
            addActor(images[i]);
            images[i].setOrigin(Align.center);
            images[i].setRotation(45 - i * 45);
            images[i].setVisible(false);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < images.length; i++) {

                    images[i].setPosition(S.u(69 - 2 - 12 + i * 12), S.u(104 + (i == 1 ? 0 : -5)));
                    images[i].setSize(S.u(5), S.u(5));
                    images[i].setVisible(true);
                    images[i].addAction(sequence(alpha(1), sizeTo(S.u(5), S.u(12), 0.2f),
                            parallel(
                                    sizeTo(S.u(5), S.u(5), 0.2f),
                                    alpha(0, 0.2f),
                                    Actions.moveBy(S.u(-12 + i * 12), S.u(12), 0.2f)
                            ),
                            hide()));
                }
            }
        };

        hand.addAction(forever(
                sequence(
                        Actions.rotateTo(2, 1f), scaleTo(0.9f, 0.9f, 0.2f), run(runnable), scaleTo(1, 1, 0.1f),
                        Actions.rotateTo(-2, 1f), scaleTo(0.9f, 0.9f, 0.2f), run(runnable), scaleTo(1, 1, 0.1f)
                )
        ));
    }
}