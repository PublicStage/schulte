package com.bear.brain.ratio.youratio;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.lib.S;

public class ErrorPopup extends Group {
    protected Image corner;
    protected Table table;
    Label label;

    public ErrorPopup(String text) {

        table = new Table();
        corner = Resources.createImage("popup_corner");
        table.setBackground(new NinePatchDrawable(Resources.createPatch("popup")));
        addActor(table);
        addActor(corner);

        label = new Label(text, Font.RUBIK14.black());
        label.setAlignment(Align.center);
        table.add(label).center().pad(S.u(8)).minWidth(S.u(120));

        table.pack();
        setSize(table.getWidth(), table.getHeight() + S.u(11));

        table.setPosition(0, S.u(11));
        corner.setPosition(S.u(32), 0);
    }

    public void show() {


    }

}
