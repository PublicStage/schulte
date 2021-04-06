package com.etheapp.brainserver.logic;

import com.bear.brain.RPage;

import java.util.HashMap;

public class TopContainer {
    public static final int PAGE_SIZE = 20;
    private TopList[] topLists;

    public TopContainer(ServerTime serverTime) {
        topLists = new TopList[Player.MAX_LEVEL - Player.MIN_LEVEL + 1];
        for (int i = 0; i < topLists.length; i++) {
            topLists[i] = new TopList(serverTime);
        }
    }

    public TopList get(int level) {
        return topLists[level - Player.MIN_LEVEL];
    }

    public int listSize(int level) {
        return get(level).size();
    }

    public void add(int level, Scores scores) {
        get(level).add(scores);
    }

    public RPage getPage(int level, int from, boolean forward, String uuid) {

        RPage page;

        TopList topList = get(level);

        page = topList.getPage(from, forward, PAGE_SIZE, uuid);
        page.setLevel(level);
        page.setUsers(topList.size());

        return page;
    }

    public void removeEventsBefore(long time) {
        for (TopList topList : topLists) {
            topList.removeEventsBefore(time);
        }
    }


    public void dispose() {
        for (TopList topList : topLists) {
            topList.dispose();
        }
    }

    public HashMap<Integer, Integer> getUsersCountByLevel() {

        HashMap<Integer, Integer> map = new HashMap<>();
        for (int level = Player.MIN_LEVEL; level <= Player.MAX_LEVEL; level++) {
            map.put(level, listSize(level));
        }

        return map;
    }

    public void setUsersCountByLevel(HashMap<Integer, Integer> users) {

    }
}
