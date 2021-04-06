package com.bear.brain.diagram3;

import com.bear.brain.Brain;
import com.bear.brain.GameResult;
import com.bear.lib.Adapter;

public class ItemAdapter extends Adapter<Item> {

    float fixedItemY;
    FixedItemListener listener;

    public ItemAdapter(FixedItemListener listener) {
        this.listener = listener;
    }

    public void setFixedItemY(float fixedItemY) {
        this.fixedItemY = fixedItemY;
    }

    @Override
    public void onBindData(Item item, int position) {
        item.setFixedItemListener(listener);
        //System.out.println("BIND " + position);

        if (position == Brain.state.valRange.getMinIndex() - 1) {
            listener.setItemVisible(false);
        }


        GameResult result = Brain.state.getGameResult(position);

        item.setTime(result.getDuration());
        item.setDate(result.getDateTime());
        item.setLength(Brain.state.valRange.nor(position), Brain.state.valRange.nor(Brain.state.valRange.getMinIndex()));
        item.setBest(Brain.state.valRange.getMinIndex() == position);
        item.setFixedItemY(fixedItemY);
    }

    @Override
    public int size() {
        return Brain.state.getResultsCount();
    }

    @Override
    public Class<Item> getType(int position) {
        return Item.class;
    }
}
