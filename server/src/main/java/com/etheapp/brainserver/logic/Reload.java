package com.etheapp.brainserver.logic;

import com.etheapp.brainserver.db.DBService;

public class Reload {
    public static void loadFromDB(GGame game) {
        if (DBService.getInstance().connectionCheck()) {
            game.load(DBService.getInstance().loadResults());
            game.getChat("ru").load(DBService.getInstance().loadChat("ru"));
            game.getChat("en").load(DBService.getInstance().loadChat("en"));
        }

    }
}
