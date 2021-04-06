package com.bear.brain.diagram3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.bear.brain.Brain;
import com.bear.brain.GameResult;
import com.bear.brain.resources.Resources;
import com.bear.lib.ImageLayer;
import com.bear.lib.Layers;

public class FixedItem extends Group {
    ImageLayer back;
    Item item;
    ImageLayer grad;

    public FixedItem() {
        setTransform(false);
        back = new ImageLayer(Resources.createPatch("main_color"));
        back.setLayerId(1);
        addActor(back);
        item = new Item();
        addActor(item);
        grad = new ImageLayer(Resources.createPatch("grad.2x"));
        grad.setLayerId(1);
        addActor(grad);
        grad.setSize(Gdx.graphics.getWidth(), grad.getHeight());
        setSize(Gdx.graphics.getWidth(), item.getHeight() + grad.getHeight());
        back.setSize(getWidth(), item.getHeight());
        back.setPosition(0, grad.getHeight());
        item.setPosition(0, grad.getHeight());
        loadValues();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        while (Layers.inc()) {
            super.draw(batch, parentAlpha);
        }
    }

    public void loadValues() {
        if (Brain.state.getResultsCount() > 0) {
            int id = Brain.state.valRange.getMinIndex();
            GameResult result = Brain.state.getGameResult(id);

            item.setTime(result.getDuration());
            item.setDate(result.getDateTime());
            item.setLength(Brain.state.valRange.nor(id), Brain.state.valRange.nor(id));
            item.setBest(true);
            item.setTracked(false);
        } else {
            setVisible(false);
        }

    }
}
