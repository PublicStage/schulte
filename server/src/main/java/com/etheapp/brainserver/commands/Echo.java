package com.etheapp.brainserver.commands;

import com.bear.brain.RequestEcho;
import com.etheapp.brainserver.logic.GGame;
import com.google.gson.Gson;

public class Echo implements Command {
    @Override
    public Object process(GGame game, String request, String ip) {
        Gson gson = new Gson();
        return gson.fromJson(request, RequestEcho.class).getParameter();
    }
}
