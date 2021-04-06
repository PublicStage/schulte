package com.etheapp.brainserver.commands;

import com.bear.brain.RequestChangeName;
import com.etheapp.brainserver.logic.GGame;
import com.google.gson.Gson;

public class ChangeName implements Command {
    @Override
    public Object process(GGame game, String request, String ip) {
        RequestChangeName r = new Gson().fromJson(request, RequestChangeName.class);
        return game.changeName(r.getUuid(), r.getName());
    }
}
