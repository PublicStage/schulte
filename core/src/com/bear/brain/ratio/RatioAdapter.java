package com.bear.brain.ratio;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.bear.brain.RItem;
import com.bear.lib.Adapter;

public class RatioAdapter extends Adapter<Actor> {

    private DualLoader loader;

    public RatioAdapter(DualLoader loader) {
        this.loader = loader;
    }

    @Override
    public void onBindData(Actor ratioItem, int position) {
        if (position <= loader.getCount() - loader.getZero()) {
            if (ratioItem instanceof RatioItem) {
                int place = loader.getZero() + position;
                RItem data = loader.get(place);
                ((RatioItem) ratioItem).setData(place, data); //TODO null data
            }
        } else {
            ((RatioItem) ratioItem).setData(loader.fromPosition() + position, null);
        }
    }

    @Override
    public int size() {
        return loader.maxPosition();
        //return loader.size() + (loader.hasMoreElements() ? 1 : 0);
    }

    @Override
    public Class getType(int position) {
        return RatioItem.class;
        //return loader.size() > 0 ? RatioItem.class : Actor.class;
        //return position < loader.size() ? RatioItem.class : loader.size() > 0 ? RatioLoad.class : Actor.class;
    }

    @Override
    public int minPosition() {
        return loader.minPosition();
    }
}