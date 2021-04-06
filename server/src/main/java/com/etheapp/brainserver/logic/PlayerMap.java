package com.etheapp.brainserver.logic;

import com.etheapp.brainserver.db.DBService;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerMap {

    ConcurrentHashMap<String, Player> map = new ConcurrentHashMap<>();

    public boolean havePlayer(String uuid) {
        return map.containsKey(uuid);
    }

    public Player getPlayer(String uuid) {
        Player player = map.get(uuid);
        if (player == null) {
            player = new Player(uuid);
            map.put(uuid, player);
            player.setName(DBService.getInstance().loadNameOrNull(uuid));
        }
        return player;
    }

    public void addScore(String uuid, int level, Score score) {
        getPlayer(uuid).addScore(level, score);
    }

    private void createPlayer(String uuid) {
        Player player = new Player(uuid);
        map.put(uuid, player);
    }
}
