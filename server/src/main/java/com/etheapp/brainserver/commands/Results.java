package com.etheapp.brainserver.commands;

import com.bear.brain.GameResult;
import com.bear.brain.RequestSendResult;
import com.bear.brain.SendResult;
import com.etheapp.brainserver.db.DBService;
import com.etheapp.brainserver.logic.GGame;
import com.etheapp.brainserver.logic.Score;
import com.google.gson.Gson;

public class Results implements Command {
    @Override
    public Object process(GGame game, String request, String ip) {
        Gson gson = new Gson();
        SendResult sendResult = gson.fromJson(request, RequestSendResult.class).getParameter();


        sendResult.timeCorrect(game.getServerTime().currentTime());
        DBService.getInstance().writeResult(sendResult); // запись б базу может тормозить сервлет

        for (GameResult gameResult : sendResult.list) {
            game.addScore(sendResult.uuid, gameResult.getLevel(), new Score(gameResult.getDateTime(), gameResult.getDuration()));
        }

        return game.getPlaces(sendResult.uuid);
    }
}
