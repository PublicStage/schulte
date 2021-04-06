package com.etheapp.brainserver.commands;

import com.bear.brain.VersionCommand;
import com.etheapp.brainserver.logic.GGame;

public class Version implements Command {
    @Override
    public Object process(GGame game, String request, String ip) {
        return new VersionCommand(1);
    }
}
