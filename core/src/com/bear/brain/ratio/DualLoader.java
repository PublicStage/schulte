package com.bear.brain.ratio;

import com.bear.brain.AnswerListener;
import com.bear.brain.Brain;
import com.bear.brain.RItem;
import com.bear.brain.RPage;
import com.bear.brain.RequestRatioList;
import com.bear.brain.net.HttpRequest;

import java.util.ArrayList;

public class DualLoader extends AnswerListener<RPage> {
    private int level;
    private int from;
    private int to;
    private int count;
    boolean load = false;

    private ArrayList<RItem> list;
    private int zero = -1;

    public DualLoader(int level) {
        this.level = level;
        list = new ArrayList<>();
        from = -1;
        to = -1;
        count = 0;
    }

    public RItem get(int position) {
        RItem item = null;
        if (notEmpty() && position >= from && position <= to) {
            item = list.get(position - from);
            if (position == to && hasMoreElements()) {
                load(to + 1, true);
            }
            if (position == from && hasBeforeElements()) {
                load(from - 1, false);
            }

        }
        return item;
    }

    public boolean hasMoreElements() {
        return to + 1 < count;
    }

    private boolean hasBeforeElements() {
        return from > 0;
    }

    public void load(int from, boolean forward) {
        if (!load) {
            load = true;
            HttpRequest.request(new RequestRatioList(level, from, forward, Brain.player.getUuid()), this);
        }
    }

    public boolean notEmpty() {
        return to != -1;
    }

    public int fromPosition() {
        return from;
    }

    public int toPosition() {
        return to;
    }

    public boolean isInit() {
        return zero >= 0;
    }

    @Override
    public void ok(RPage answer) {

        if (answer.isEmpty()) {
            reset();
            load = false;
            return;
        }

        if (isInit()) {
            if (answer.getFrom() == to + 1) {
                list.addAll(answer.getList());
                to = answer.getFrom() + answer.getList().size() - 1;
            } else if (answer.getFrom() == from - 1) {
                list.addAll(0, answer.getList());
                from = answer.getFrom() - answer.getList().size() + 1;
            }
        } else {
            list = answer.getList();
            zero = answer.getFrom();
            from = answer.getFrom();
            to = answer.getFrom() + answer.getList().size() - 1;

        }

        count = answer.getLength();
        load = false;
    }

    @Override
    public void cancel() {
        load = false;
    }

    public int maxPosition() {
        return list.size() + from - zero;
    }

    public int getCount() {
        return count;
    }

    public int getZero() {
        return zero;
    }

    public int getOwnerPosition() {
        for (int pos = 0; pos < list.size(); pos++) {
            if (list.get(pos).isOwner()) return pos + from - zero;
        }
        return -1;
    }

    public int minPosition() {
        return from - zero;
    }

    public void reset() {
        list.clear();
        from = -1;
        to = -1;
        count = 0;
        zero = -1;
    }
}
