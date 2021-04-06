package com.etheapp.brainserver.commands;

import com.etheapp.brainserver.logic.GGame;

public class Unknown implements Command {
    @Override
    public String process(GGame game, String request, String ip) {
        return "Unknown Command";
    }
}
