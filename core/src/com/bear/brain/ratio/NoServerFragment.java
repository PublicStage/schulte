package com.bear.brain.ratio;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.Texts;
import com.bear.brain.main.Caption;
import com.bear.brain.resources.Resources;
import com.bear.lib.Fragment;
import com.bear.lib.S;

public class NoServerFragment extends Fragment {
    Image dark;
    Caption caption;

    public NoServerFragment() {
        dark = new Image(Resources.createPatch("black24"));
        addActor(dark);
        caption = new Caption();
        caption.setText(Texts.SERVER_NOT_AVAILABLE.toString());
        caption.setSize(S.u(300), S.u(300));
        addActor(caption);

    }

    @Override
    protected void sizeChanged() {
        dark.setSize(getWidth(), getHeight());
        caption.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
    }
}
