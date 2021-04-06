package com.etheapp.brainserver.logic;

import com.bear.brain.ChangeNameResult;
import com.bear.brain.ChangeNameType;
import com.bear.brain.Places;
import com.bear.brain.RPage;
import com.etheapp.brainserver.Log;
import com.etheapp.brainserver.db.DBService;
import com.etheapp.brainserver.db.ResultRecord;
import com.etheapp.brainserver.logic.chat.Chat;

import java.util.List;

public class GGame {
    private ServerTime serverTime;
    private PlayerMap playerMap;
    private TopContainer topContainer;
    private Chat chatRu;
    private Chat chatEn;

    public GGame(ServerTime serverTime) {
        this.serverTime = serverTime;
        createMapAndTopLists();
    }

    public TopContainer getTopContainer() {
        return topContainer;
    }

    private void createMapAndTopLists() {
        playerMap = new PlayerMap();
        topContainer = new TopContainer(serverTime);
        chatRu = new Chat();
        chatEn = new Chat();
    }

    public ServerTime getServerTime() {
        return serverTime;
    }

    public Player getPlayer(String uuid) {
        return playerMap.getPlayer(uuid);
    }

    public Places getPlaces(String uuid) {
        Places result = new Places();
        Player player = getPlayer(uuid);
        for (int level = Player.MIN_LEVEL; level <= Player.MAX_LEVEL; level++) {
            if (player.hasScores(level)) {
                result.getPlaces().put(level, player.getScores(level).getPlace());
            }
            if (topContainer.listSize(level) > 0) {
                result.getUsers().put(level, topContainer.listSize(level));
            }
        }
        return result;
    }

    public Chat getChat(String locale) {
        return locale.equals("ru") ? chatRu : chatEn;
    }

    public void addScore(String uuid, int level, Score score) {
        Player player = playerMap.getPlayer(uuid);
        if (player.hasName()) {
            Scores scores = playerMap.getPlayer(uuid).addScore(level, score);

            topContainer.add(level, scores);
        }
    }

    public void dispose() {
        topContainer.dispose();
    }

    public void reset() {
        dispose();
        createMapAndTopLists();
    }

    public void load(List<ResultRecord> list) {
        reset();
        addScoreList(list);
        Log.info("Load %d results from DB", list.size());
    }

    public void addScoreList(List<ResultRecord> list) {
        for (ResultRecord resultRecord : list) {
            addScore(resultRecord.uuid, resultRecord.level, new Score(resultRecord.date, resultRecord.result));
        }
    }

    public RPage getPage(int level, int from, boolean forward, String uuid) {
        RPage page = getTopContainer().getPage(level, from, forward, uuid);
        if (uuid != null) {
            page.setPlace(getPlayer(uuid).getPlace(level));
        }

        return page;
    }

    public ChangeNameResult changeName(String uuid, String name) {
        if (name != null) {

            if (DBService.getInstance().nameExists(name)) {
                return new ChangeNameResult(ChangeNameType.DUPLICATE);
            }

            DBService.getInstance().writeName(uuid, name);

            Player player = getPlayer(uuid);
            boolean loadResults = !player.hasName();
            player.setName(name);
            if (loadResults) {
                List<ResultRecord> results = DBService.getInstance().loadUuidResults(uuid);
                addScoreList(results);
                Log.info("ChangeName add %s and load %d results from DB", name, results.size());
            }

            return new ChangeNameResult(name, getPlaces(uuid));
        } else {
            return new ChangeNameResult(ChangeNameType.ERROR);
        }
    }
}
