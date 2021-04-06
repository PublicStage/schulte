package com.bear.brain.resources;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.bear.lib.S;

public enum Font {
    RUBIK10("rubik10.ttf", "fonts/Rubik-Medium.ttf", 11),
    RUBIK14("rubik14.ttf", "fonts/Rubik-Medium.ttf", 15),
    RUBIK48("rubik48.ttf", "fonts/Rubik-Medium.ttf", 26);

    String name;
    String ttf;
    int size;
    boolean border;

    Font(String name, String ttf, int size) {
        this.name = name;
        this.ttf = ttf;
        this.size = S.u(size);
        this.border = false;
    }

    Font(String name, String ttf, int size, boolean border) {
        this.name = name;
        this.ttf = ttf;
        this.size = size;
        this.border = border;
    }

    public Label.LabelStyle style(Color color) {
        return new Label.LabelStyle(Resources.manager.get(name, BitmapFont.class), color);
    }

    public Label.LabelStyle black() {
        return style(Color.BLACK);
    }

    public Label.LabelStyle white() {
        return style(Color.WHITE);
    }

    public BitmapFont font() {
        return Resources.manager.get(name, BitmapFont.class);
    }

}
