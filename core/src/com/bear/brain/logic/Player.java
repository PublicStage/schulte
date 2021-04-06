package com.bear.brain.logic;

import com.bear.brain.AnswerListener;
import com.bear.brain.Brain;
import com.bear.brain.InitInfo;
import com.bear.brain.Places;
import com.bear.brain.RequestLogin;
import com.bear.brain.Version;
import com.bear.brain.events.ChangePlace;
import com.bear.brain.net.HttpRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.UUID;

public class Player extends AnswerListener<InitInfo> {
    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 15;

    private String uuid;
    private String name;
    private int[] place = new int[State.MAX_LEVEL - State.MIN_LEVEL + 1];
    private int[] users = new int[place.length];
    private boolean chatNeedUpdate;
    private boolean ratingNeedUpdate;

    public Player() {
        resetPlaces();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? UUID.randomUUID().toString() : uuid;
    }

    public void sendLogin() {
        HttpRequest.request(new RequestLogin(getUuid(), Version.VERSION), this);
    }

    public static boolean checkNameLength(String str) {
        return str != null && str.length() >= MIN_NAME_LENGTH && str.length() <= MAX_NAME_LENGTH;
    }

    public static boolean checkNameDifference(String str) {
        return (Brain.player.getName() == null && !str.isEmpty()) || str != null && Brain.player.getName() != null && !str.contentEquals(Brain.player.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int getPlace(int level) {
        return place[level - State.MIN_LEVEL];
    }

    public int getUsersCountOnCurrentLevel() {
        return getUsersCount(Brain.state.getSize());
    }

    public int getPlaceOnCurrentLevel() {
        return getPlace(Brain.state.getSize());
    }

    public boolean haveResultOnCurrentLevel() {
        return getPlaceOnCurrentLevel() > 0;
    }

    public boolean haveUsersOnCurrentLevel() {
        return getUsersCountOnCurrentLevel() > 0;
    }

    public void setPlace(int level, int value, int userCount) {
        if (level >= State.MIN_LEVEL && level <= State.MAX_LEVEL && (getPlace(level) != value || getUsersCount(level) != userCount)) {
            place[level - State.MIN_LEVEL] = value;
            users[level - State.MIN_LEVEL] = userCount;
            EventBus.getDefault().post(new ChangePlace(level, value));
        }
    }

    public void reset() {
        setName(null);
        setUuid(null);

        resetPlaces();
    }

    private void resetPlaces() {
        for (int level = State.MIN_LEVEL; level <= State.MAX_LEVEL; level++) setPlace(level, 0, 0);
    }

    @Override
    public void ok(InitInfo answer) {
        setName(answer.getName());
        for (Integer level : answer.getUsers().keySet()) {
            int placeOnLevel = answer.getPlaces().containsKey(level) ? answer.getPlaces().get(level) + 1 : 0;
            int usersOnLevel = answer.getUsers().get(level);
            setPlace(level, placeOnLevel, usersOnLevel);
        }
        setChatNeedUpdate(Version.needUpdate(answer.getChatMinVersion()));
        setRatingNeedUpdate(Version.needUpdate(answer.getRatingMinVersion()));

    }

    public void setUsers(HashMap<Integer, Integer> map) {
        for (Integer level : map.keySet()) {
            users[level - State.MIN_LEVEL] = map.get(level);
        }
    }

    public void setPlaces(Places places) {
        for (Integer level : places.getPlaces().keySet()) {
            setPlace(level, places.getPlaces().get(level) + 1, places.getUsers().get(level));
        }
    }

    public int getUsersCount(int level) {
        return users[level - State.MIN_LEVEL];
    }

    @Override
    public void cancel() {

    }

    public boolean isChatNeedUpdate() {
        return chatNeedUpdate;
    }

    public void setChatNeedUpdate(boolean chatNeedUpdate) {
        this.chatNeedUpdate = chatNeedUpdate;
    }

    public boolean isRatingNeedUpdate() {
        return ratingNeedUpdate;
    }

    public void setRatingNeedUpdate(boolean ratingNeedUpdate) {
        this.ratingNeedUpdate = ratingNeedUpdate;
    }
}
