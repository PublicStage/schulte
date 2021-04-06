package com.bear.brain.ratio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Array;
import com.bear.brain.AnswerListener;
import com.bear.brain.Brain;
import com.bear.brain.RItem;
import com.bear.brain.RPage;
import com.bear.brain.RequestRatioList;
import com.bear.brain.net.HttpRequest;

public class RatioLoader extends AnswerListener<RPage> {
    int level;
    Array<RItem> list = new Array<>();
    boolean hasMoreElements = true;
    Net.HttpRequest request;

    public Array<RItem> getList() {
        return list;
    }

    public int size() {
        return list.size;
    }

    public void load(int level, int from) {
        this.level = level;
        request = HttpRequest.request(new RequestRatioList(level, from, true, Brain.player.getUuid()), this);
    }

    @Override
    public void ok(RPage page) {
        if (page.getFrom() == 0) {
            list.clear(); // TODO сделать перезапись с элемента page.getFrom()
        }
        for (RItem item : page.getList()) {
            list.add(item);
        }
        hasMoreElements = page.getLength() > size();
        Brain.player.setPlace(page.getLevel(), page.getPlace() + 1, page.getUsers());
    }

    @Override
    public void cancel() {

    }

    public boolean isHasMoreElements() {
        return hasMoreElements;
    }

    public void dispose() {
        if (request != null) {
            Gdx.net.cancelHttpRequest(request);
            request = null;
        }
    }

    public RItem get(int position) {
        return list.get(position);
    }

    public void setLevel(int level) {
        this.level = level;
        list.clear();
        hasMoreElements = true;
    }

    public int getOwnerPosition() {
        for (int pos = 0; pos < list.size; pos++) {
            if (list.get(pos).isOwner()) return pos;
        }
        return -1;
    }
}
