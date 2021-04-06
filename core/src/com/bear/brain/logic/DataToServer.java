package com.bear.brain.logic;

import com.bear.brain.AnswerListener;
import com.bear.brain.Brain;
import com.bear.brain.GameResult;
import com.bear.brain.Places;
import com.bear.brain.RequestSendResult;
import com.bear.brain.SendResult;
import com.bear.brain.net.HttpRequest;

import java.util.ArrayList;

public class DataToServer extends AnswerListener<Places> implements StateListener {
    ArrayList<GameResult> list = new ArrayList<>();
    ArrayList<GameResult> sendQueue = new ArrayList<>();

    public ArrayList<GameResult> getList() {
        return list;
    }

    @Override
    public void start() {

    }

    @Override
    public void finish(GameResult result) {
        if (result.isValid()) {
            list.add(result);
            sendQueueIfHave();
        }
    }

    @Override
    public void press(int id) {

    }

    @Override
    public void changeLevel(int level) {

    }

    public void load(ArrayList<GameResult> listToServer) {
        list.clear();
        list.addAll(listToServer);
    }

    public void reset() {
        sendQueue.clear();
        list.clear();
    }

    @Override
    public void ok(Places answer) {
        sendQueue.clear();
        Brain.player.setUsers(answer.getUsers());
        for (Integer level : answer.getPlaces().keySet()) {
            Brain.player.setPlace(level, answer.getPlaces().get(level) + 1, answer.users.get(level));
        }

    }

    @Override
    public void cancel() {
        list.addAll(sendQueue);
        sendQueue.clear();
    }

    public void sendQueueIfHave() {
        if (!list.isEmpty() && !Brain.player.isRatingNeedUpdate()) {
            sendQueue.addAll(list);
            list.clear();
            HttpRequest.request(new RequestSendResult(new SendResult(Brain.player.getUuid(), State.deviceTime.getTime(), sendQueue)), this);
        }
    }
}
