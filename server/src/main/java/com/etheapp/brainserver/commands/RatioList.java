package com.etheapp.brainserver.commands;

import com.bear.brain.RequestRatioList;
import com.etheapp.brainserver.logic.GGame;
import com.google.gson.Gson;

public class RatioList implements Command {
    @Override
    public Object process(GGame game, String request, String ip) {
        RequestRatioList re = new Gson().fromJson(request, RequestRatioList.class);

        // TODO добавить в имплементацию Command параметр типа ответа

        return game.getPage(re.getLevel(), re.getFrom(), re.isForward(), re.getUuid());
    }
}
