package com.etheapp.brainserver.logic;

public class TestUsers {
    public static void addTestUsers(GGame game) {
        for (int i = 1; i <= 100; i++) {
            String uuid = "uuid" + i;
            game.getPlayer(uuid).setName("User " + i + " name");
            game.addScore(uuid, 3, new Score(System.currentTimeMillis(), (long) (3000 + i * 10)));
            game.addScore(uuid, 4, new Score(System.currentTimeMillis(), (long) (15000 + Math.random() * 5000)));
            game.addScore(uuid, 5, new Score(System.currentTimeMillis(), (long) (60000 + Math.random() * 10000)));
        }
    }
}
