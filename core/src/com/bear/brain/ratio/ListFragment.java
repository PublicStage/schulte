package com.bear.brain.ratio;

import com.bear.brain.Brain;
import com.bear.brain.RItem;
import com.bear.brain.RPage;
import com.bear.brain.events.ChangeName;
import com.bear.brain.events.OnLoadUserRatioItem;
import com.bear.brain.events.ScrollToPlace;
import com.bear.lib.Fragment;
import com.bear.lib.RecyclerDX;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    DualLoader loader;
    RecyclerDX rx;
    private boolean loadFirstItems = false;

    public ListFragment() {
        loader = new DualLoader(Brain.state.getSize()) {
            @Override
            public void ok(RPage page) {
                int positionOfLastItem = loader.maxPosition();
                super.ok(page);

                if (page.isEmpty() && page.getLength() > 0) {
                    loadFirstItems = false;
                    loader.load(0, true);
                } else if (!loadFirstItems) {
                    loadFirstItems = true;
                    rx.scrollToElement(0);
                } else {
                    if (rx.isAddedEmptyBottom()) {
                        rx.scrollToBack();
                    } else {
                        //rx.update();
                    }

                }
                rx.setVisible(loader.maxPosition() != 0);
                Brain.player.setPlace(page.getLevel(), page.getPlace() + 1, page.getUsers());

                if (isLoadedOwnResult(page.getList())) {
                    EventBus.getDefault().post(new OnLoadUserRatioItem());
                }
            }
        };
        rx = new RecyclerDX(new RatioAdapter(loader));
        addActor(rx);

        EventBus.getDefault().register(this);

        loader.load(0, true);

//        int place = Brain.player.getPlace(Brain.state.getSize());
//        if (place > 0) {
//            loader.load(Math.max(0, place - 3), true);
//        } else {
//            loader.load(0, true);
//        }
    }

    public boolean isLoadedOwnResult(ArrayList<RItem> list) {
        for (int pos = 0; pos < list.size(); pos++) {
            if (list.get(pos).isOwner()) return true;
        }
        return false;
    }

    @Subscribe
    public void onMessageEvent(ChangeName event) {
        //rx.update(loader.getOwnerPosition());
        rx.update();
    }

    @Subscribe
    public void onMessageEvent(ScrollToPlace event) {
        loader.reset();
        loadFirstItems = false;
        loader.load(Math.max(0, Brain.player.getPlaceOnCurrentLevel() - 4), true);
    }

    @Override
    public void dispose() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void sizeChanged() {
        rx.setSize(getWidth(), getHeight());
    }
}
