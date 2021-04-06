package com.etheapp.brainserver;

import com.bear.brain.RequestCommand;
import com.bear.brain.logic.coding.Coder;
import com.etheapp.brainserver.commands.ChangeName;
import com.etheapp.brainserver.commands.ChatMessages;
import com.etheapp.brainserver.commands.Command;
import com.etheapp.brainserver.commands.Echo;
import com.etheapp.brainserver.commands.Login;
import com.etheapp.brainserver.commands.Message;
import com.etheapp.brainserver.commands.RatioList;
import com.etheapp.brainserver.commands.Results;
import com.etheapp.brainserver.commands.Unknown;
import com.etheapp.brainserver.commands.Version;
import com.etheapp.brainserver.logic.GGame;
import com.google.gson.Gson;

import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class CommandController {
    public static String process(GGame game, String json, String ip) throws BadPaddingException, IllegalBlockSizeException, IOException {
        return Coder.encode(parseCommand(json).process(game, json, ip));
    }

    private static Command parseCommand(String json) {
        switch (new Gson().fromJson(json, RequestCommand.class).getCommand()) {
            case "login":
                return new Login();
            case "version":
                return new Version();
            case "echo":
                return new Echo();
            case "sendresult":
                return new Results();
            case "ratiolist":
                return new RatioList();
            case "changename":
                return new ChangeName();
            case "chat":
                return new ChatMessages();
            case "sendMessage":
                return new Message();
            default:
                return new Unknown();
        }
    }
}
