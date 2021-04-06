package com.bear.brain;

import com.badlogic.gdx.Gdx;
import com.bear.brain.coding.Coder;
import com.bear.brain.logic.LevelsTimes;
import com.bear.brain.logic.ResultInterval;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class Save {
    public static String SAVES = "shulte";
    private static final String LEVEL = "level";
    private static final String PROGRESS = "pr";
    private static final String LOG = "log";
    private static final String TIMERS = "timers";
    private static final String USER = "user";
    private static final String TOSERVER = "toserver";
    private static final String SOUND = "sound";
    private static final String BLOCK = "block";

    private static final Gson gson = new Gson();


    public static void saveLevel(int level) {
        Gdx.app.getPreferences(SAVES).putInteger(LEVEL, level).flush();
    }

    public static int loadLevel() {
        return Gdx.app.getPreferences(SAVES).getInteger(LEVEL, 3);
    }

    public static void saveProgress() {
        try {
            LevelsData data = new LevelsData();
            for (int level : Brain.state.timeLog.logByLevel.keySet()) {
                ResultInterval resultInterval = Brain.state.timeLog.logByLevel.get(level);
                for (int i = 0; i < resultInterval.size(); i++) {
                    GameResult gameResult = resultInterval.getResult(i);
                    data.add(level, gameResult.getDateTime(), gameResult.getDuration());
                }
            }

            data.listToServer.addAll(Brain.state.dataToServer.getList());
            Brain.state.dataToServer.load(data.listToServer);

            //Gdx.app.log(TAG, "SAVE " + gson.toJson(data));

            Gdx.app.getPreferences(SAVES).putString(PROGRESS, Coder.encodeZip(gson.toJson(data))).flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadProgress() {
        if (Gdx.app.getPreferences(SAVES).contains(PROGRESS)) {
            Brain.state.timeLog.reset();
            try {

                //Gdx.app.log(TAG, "LOAD " + Coder.decodeZip(Gdx.app.getPreferences(SAVES).getString(PROGRESS)));

                LevelsData data = gson.fromJson(Coder.decodeZip(Gdx.app.getPreferences(SAVES).getString(PROGRESS)), LevelsData.class);
                for (int level : data.map.keySet()) {
                    for (Result result : data.map.get(level)) {
                        Brain.state.timeLog.loadGameResult(level, result.date, result.duration);
                    }
                    Brain.state.timeLog.sort(level);
                }
                Brain.state.timeLog.removeOld(Brain.state.getSize());
                Brain.state.valRange.updateMinMax();
                Brain.state.setLastValueToTimer();
                Brain.state.dataToServer.load(data.listToServer);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadUser() {
        if (Gdx.app.getPreferences(SAVES).contains(USER)) {
            try {
                User user = gson.fromJson(Coder.decodeZip(Gdx.app.getPreferences(SAVES).getString(USER)), User.class);
                Brain.player.setUuid(user.uuid);
                Brain.player.setName(user.name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Brain.player.setUuid(UUID.randomUUID().toString());
        }
    }

    public static void saveUser() {
        try {
            User user = new User();
            user.uuid = Brain.player.getUuid();
            user.name = Brain.player.getName();
            Gdx.app.getPreferences(SAVES).putString(USER, Coder.encodeZip(gson.toJson(user))).flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadBlock() {
        if (Gdx.app.getPreferences(SAVES).contains(BLOCK)) {
            try {
                Block block = gson.fromJson(Coder.decodeZip(Gdx.app.getPreferences(SAVES).getString(BLOCK)), Block.class);
                Brain.player.setRatingNeedUpdate(block.ratingNeedUpdate);
                Brain.player.setChatNeedUpdate(block.chatNeedUpdate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveBlock() {
        try {
            Block block = new Block();
            block.ratingNeedUpdate = Brain.player.isRatingNeedUpdate();
            block.chatNeedUpdate = Brain.player.isChatNeedUpdate();
            Gdx.app.getPreferences(SAVES).putString(BLOCK, Coder.encodeZip(gson.toJson(block))).flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/*
    public static void saveLog() {
        String g = gson.toJson(Brain.state.resultsLog);
        Gdx.app.log(TAG, "SAVE LOG " + Brain.state.resultsLog.size() + " elements");
        try {
            Gdx.app.getPreferences(SAVES).putString(LOG, Coder.encodeZip(g)).flush();
        } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    public static void loadLog() {
        if (Gdx.app.getPreferences(SAVES).contains(LOG)) {
            try {
                ResultsLog log = gson.fromJson(Coder.decodeZip(Gdx.app.getPreferences(SAVES).getString(LOG)), ResultsLog.class);
                Gdx.app.log(TAG, "LOAD LOG " + log.size() + " elements");
                Brain.state.resultsLog.copyFrom(log);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
*/

    public static void saveTimings() {
        String g = gson.toJson(Brain.state.pressTimer.getLevels());
        //Gdx.app.log(TAG, "SAVE TIMINGS " + g);
        try {
            Gdx.app.getPreferences(SAVES).putString(TIMERS, Coder.encodeZip(g)).flush();
        } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

    }

    public static void loadTimings() {
        if (Gdx.app.getPreferences(SAVES).contains(TIMERS)) {
            try {
                LevelsTimes times = gson.fromJson(Coder.decodeZip(Gdx.app.getPreferences(SAVES).getString(TIMERS)), LevelsTimes.class);
                //Gdx.app.log(TAG, "LOAD TIMERS " + Coder.decodeZip(Gdx.app.getPreferences(SAVES).getString(TIMERS)));
                Brain.state.pressTimer.setLevels(times);
                Brain.state.pressTimer.changeLevel(Brain.state.getSize());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean loadSound() {
        return Gdx.app.getPreferences(SAVES).getBoolean(SOUND, true);
    }

    public static void saveSound(boolean value) {
        Gdx.app.getPreferences(SAVES).putBoolean(SOUND, value).flush();
    }

}
