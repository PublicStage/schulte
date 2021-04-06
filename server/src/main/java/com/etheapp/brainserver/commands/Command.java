package com.etheapp.brainserver.commands;

import com.etheapp.brainserver.logic.GGame;

public interface Command {
    Object process(GGame game, String request, String ip);
}
