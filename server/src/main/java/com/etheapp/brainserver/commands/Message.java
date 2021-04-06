package com.etheapp.brainserver.commands;

import com.bear.brain.ChatMessage;
import com.bear.brain.SendMessage;
import com.etheapp.brainserver.db.DBService;
import com.etheapp.brainserver.logic.GGame;
import com.etheapp.brainserver.logic.Player;
import com.google.gson.Gson;

public class Message implements Command {

    @Override
    public Object process(GGame game, String request, String ip) {
        Gson gson = new Gson();
        SendMessage sendMessage = gson.fromJson(request, SendMessage.class);

        System.out.println("Message process " + sendMessage.getMessage());

        Player player = game.getPlayer(sendMessage.getUuid());
        if (player.hasName()) {
            int bestPlace = player.getBestPlace() < Integer.MAX_VALUE ? player.getBestPlace() + 1 : 0;
            ChatMessage chatMessage = new ChatMessage(System.currentTimeMillis(), player.getName(), sendMessage.getMessage(), bestPlace);
            game.getChat(sendMessage.getLocale()).addMessage(chatMessage);
            DBService.getInstance().writeChatMessage(sendMessage.getUuid(), sendMessage.getLocale(), chatMessage);
        }

        return "ok";
    }
}
