package com.bear.brain.chat;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.bear.lib.Adapter;

public class ChatAdapter extends Adapter<WidgetGroup> {

    ChatLoader loader = new ChatLoader();

    public ChatAdapter() {
        loader.loadFirst();
    }

    @Override
    public void onBindData(WidgetGroup chatItem, int position) {
        if (loader.isMessage(position)) {
            ((Item) chatItem).setMessage(loader.get(position));
        } else {
            ((ItemDate) chatItem).setDate(loader.get(position).getTime());
        }
        if (position == loader.getZeroId() && position > loader.getMin()) {
            loader.loadPrev();
        }
    }

    @Override
    public int size() {
        return loader.getMax();
    }

    @Override
    public int minPosition() {
        return loader.getZeroId();
    }

    @Override
    public Class getType(int position) {
        return loader.isMessage(position) ? Item.class : ItemDate.class;
    }

    public void check() {
        loader.loadNext();
    }
}
