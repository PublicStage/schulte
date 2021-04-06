package com.etheapp.brainserver.logic;

import com.bear.brain.RItem;
import com.bear.brain.RPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TopList implements ClearDate {
    private final List<Scores> list = new ArrayList<>();
    private final ClearTask clearTask;
    private final ReadWriteLock topLock = new ReentrantReadWriteLock();

    public TopList(ServerTime serverTime) {
        clearTask = new ClearTask(new Timer(), this, serverTime);
    }

    public int size() {
        return list.size();
    }

    public void add(Scores scores) {
        if (scores.size() == 0) return;
        topLock.writeLock().lock();
        try {
            if (scores.size() == 1) list.add(scores);
            sort();
            clearTask.startTask();
        } finally {
            topLock.writeLock().unlock();
        }
    }

    private void sort() {
        list.sort(Scores::compareResult);
        for (int place = 0; place < list.size(); place++) {
            list.get(place).setPlace(place);
        }
    }

    public RPage getPage(int from, int count) {
        return getPage(from, true, count, null);
    }

    public RPage getPage(int from, boolean forward, int count, String uuid) {
        ArrayList<RItem> dataList = new ArrayList<>();
        int size = 0;
        topLock.readLock().lock();
        try {
            if (from >= 0 && from < list.size()) {
                if (forward) {
                    for (int i = from, to = Math.min(list.size(), from + count); i < to; i++) {
                        Scores scores = list.get(i);
                        dataList.add(new RItem(scores.getPlayer().getName(), scores.getBest().getValue(), scores.getPlayer().getUuid().equals(uuid)));
                    }
                } else {
                    for (int i = Math.max(0, from - count); i <= from; i++) {
                        Scores scores = list.get(i);
                        dataList.add(new RItem(scores.getPlayer().getName(), scores.getBest().getValue(), scores.getPlayer().getUuid().equals(uuid)));
                    }
                }
                size = list.size();
            }

        } finally {
            topLock.readLock().unlock();
        }
        return new RPage(dataList, from, size);
    }


    public List<Scores> getListCopy() {
        topLock.readLock().lock();
        try {
            return new ArrayList<>(list);
        } finally {
            topLock.readLock().unlock();
        }
    }

    @Override
    public long getFirstDateOrZero() {
        topLock.readLock().lock();
        try {
            if (list.isEmpty()) return 0;
            Scores first = list.get(0);
            for (Scores user : list) {
                if (user.getFirstScoreDate() < first.getFirstScoreDate()) {
                    first = user;
                }
            }
            return first.getFirstScoreDate();
        } finally {
            topLock.readLock().unlock();
        }
    }

    public void removeEventsBefore(long time) {
        topLock.writeLock().lock();
        try {
            Scores first = getFirstByDate();
            while (first != null && first.getFirstScoreDate() <= time) {
                first.removeFirstScore();
                if (first.hasNoScores()) {
                    list.remove(first);
                    first.resetPlace();
                    //removeUser(first.getUuid());
                    first = getFirstByDate();
                }
            }
            sort();
        } finally {
            topLock.writeLock().unlock();
        }
    }

    private Scores getFirstByDate() {
        topLock.readLock().lock();
        try {
            if (list.isEmpty()) return null;
            Scores first = list.get(0);
            for (Scores user : list) {
                if (user.getFirstScoreDate() < first.getFirstScoreDate()) {
                    first = user;
                }
            }
            return first;

        } finally {
            topLock.readLock().unlock();
        }
    }

    public void dispose() {
        clearTask.dispose();
    }

    public long getMinTime() {
        topLock.readLock().lock();
        try {
            if (list.isEmpty()) return 0;
            Scores first = list.get(0);
            return first.getBest().getValue();
        } finally {
            topLock.readLock().unlock();
        }
    }

    public long getMaxTime() {
        topLock.readLock().lock();
        try {
            if (list.isEmpty()) return 0;
            Scores last = list.get(list.size() - 1);
            return last.getBest().getValue();
        } finally {
            topLock.readLock().unlock();
        }
    }
}
