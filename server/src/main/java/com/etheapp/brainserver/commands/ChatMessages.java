package com.etheapp.brainserver.commands;

import com.bear.brain.RequestChat;
import com.etheapp.brainserver.logic.GGame;
import com.google.gson.Gson;

public class ChatMessages implements Command {
    public static final int PAGE_SIZE = 20;

    @Override
    public Object process(GGame game, String request, String ip) {
        RequestChat requestChat = new Gson().fromJson(request, RequestChat.class);


        if (requestChat.getLastId() == -1) {
            return game.getChat(requestChat.getLocale()).getLastPage(PAGE_SIZE);
        } else if (requestChat.isForward()) {
            return game.getChat(requestChat.getLocale()).getPage(requestChat.getLastId() + 1, PAGE_SIZE, true);
        } else {
            return game.getChat(requestChat.getLocale()).getPage(requestChat.getLastId() - 1, PAGE_SIZE, false);
        }
    }
}
