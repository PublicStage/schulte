package com.etheapp.brainserver.commands;

import com.bear.brain.InitInfo;
import com.bear.brain.RequestLogin;
import com.etheapp.brainserver.Version;
import com.etheapp.brainserver.db.DBService;
import com.etheapp.brainserver.logic.GGame;
import com.etheapp.brainserver.logic.Player;
import com.google.gson.Gson;

public class Login implements Command {
    @Override
    public Object process(GGame game, String request, String ip) {
        RequestLogin r = new Gson().fromJson(request, RequestLogin.class);

        String uuid = r.getUuid();
        int version = r.getVersion();


        if (uuid != null) {
            //Commands.writeLogin(uuid);

            DBService.getInstance().writeLogin(uuid, version, ip);
            DBService.getInstance().writeStateLogin(uuid, ip);

            Player player = game.getPlayer(uuid);

            InitInfo result = new InitInfo();
            result.setName(player.getName());

            result.setUsers(game.getTopContainer().getUsersCountByLevel());
            result.setPlaces(player.getPlacesByLevel());
            result.setChatMinVersion(Version.CHAT_MIN_VERSION);
            result.setRatingMinVersion(Version.RATING_MIN_VERSION);

            return result;
        } else {
            return "error";
        }

    }
}
