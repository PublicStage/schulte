package com.bear.brain;

import com.etheapp.brainserver.logic.GGame;
import com.etheapp.brainserver.logic.Player;
import com.etheapp.brainserver.logic.PlayerMap;
import com.etheapp.brainserver.logic.Score;
import com.etheapp.brainserver.logic.Scores;
import com.etheapp.brainserver.logic.ServerTime;
import com.etheapp.brainserver.logic.TopList;
import com.etheapp.brainserver.logic.chat.Chat;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class TestNewLogic {


    @BeforeClass
    public static void beforeClass() throws Exception {
        DBServiceOneConnection.createInstance();
    }

    @Test
    public void testScore() {
        Score score1 = new Score(1, 1000);
        Score score2 = new Score(2, 2000);

        assertTrue(score1.isBetter(score2));
        assertFalse(score2.isBetter(score1));
        assertTrue(score1.equal(score1));
    }

    @Test
    public void testScores() {
        Score score1 = new Score(1, 1000);
        Score score2 = new Score(2, 2000);
        Score score3 = new Score(2, 3000);

        Scores scores = new Scores(new Player("uuid"));
        assertEquals(0, scores.size());

        scores.add(score2);
        assertTrue(scores.getBest().equal(score2));
        scores.add(score3);
        scores.add(score1);
        assertTrue(scores.getBest().equal(score1));
    }

    @Test
    public void testTopList() {
        ServerTime time = new ServerTime() {
            @Override
            public long currentTime() {
                return 1;
            }
        };
        TopList topList = new TopList(time);
        assertEquals(0, topList.size());

        Player player = new Player("uuid");
        topList.add(new Scores(player));
        assertEquals(0, topList.size());
        player.addScore(3, new Score(1, 1000));
        topList.add(player.getScores(3));
        assertEquals(1, topList.size());


        GGame game = new GGame(time);
        game.getPlayer("user1").setName("user1");
        game.getPlayer("user2").setName("user2");


        game.addScore("user1", 3, new Score(1, 3000));
        assertEquals("user1", game.getTopContainer().get(3).getPage(0, 10).getList().get(0).getName());
        game.addScore("user2", 3, new Score(1, 2000));
        assertEquals("user2", game.getTopContainer().get(3).getPage(0, 10).getList().get(0).getName());
        assertEquals("user1", game.getTopContainer().get(3).getPage(0, 10).getList().get(1).getName());
        game.addScore("user1", 3, new Score(2, 1000));

        assertEquals(2, game.getTopContainer().listSize(3));

        assertEquals("user1", game.getTopContainer().get(3).getPage(0, 10).getList().get(0).getName());
        assertEquals("user2", game.getTopContainer().get(3).getPage(0, 10).getList().get(1).getName());

    }

    @Test
    public void testAddRemoveScores() {
        ServerTime time = new ServerTime() {
            @Override
            public long currentTime() {
                return 1;
            }
        };
        GGame game = new GGame(time);
        assertEquals(0, game.getTopContainer().listSize(3));
        Player player = game.getPlayer("uuid");
        player.setName("user");
        assertNull(player.getScores(3));
        game.addScore("uuid", 3, new Score(1, 1000));
        assertEquals(1000, game.getPlayer("uuid").getScores(3).getBest().getValue());
        assertEquals(1, game.getTopContainer().listSize(3));

        game.getTopContainer().removeEventsBefore(1);
        assertEquals(0, game.getTopContainer().listSize(3));

        game.getPlayer("uuid1").setName("user1");
        game.getPlayer("uuid2").setName("user2");
        game.addScore("uuid1", 3, new Score(5, 1000));
        game.addScore("uuid2", 3, new Score(6, 1000));
        game.getTopContainer().removeEventsBefore(1);
        assertEquals(2, game.getTopContainer().listSize(3));
        game.getTopContainer().removeEventsBefore(5);
        assertEquals(1, game.getTopContainer().listSize(3));
        game.getTopContainer().removeEventsBefore(6);
        assertEquals(0, game.getTopContainer().listSize(3));
    }

    @Test
    public void testClearOld() throws InterruptedException {
        AtomicLong time = new AtomicLong(TimeUnit.DAYS.toMillis(2));
        ServerTime servertime = new ServerTime() {
            @Override
            public long currentTime() {
                return time.get();
            }
        };
        GGame game = new GGame(servertime);
        game.getPlayer("uuid").setName("user");
        game.addScore("uuid", 3, new Score(time.get() - TimeUnit.DAYS.toMillis(1), 1000));
        Thread.sleep(10);
        assertEquals(0, game.getTopContainer().listSize(3));
        assertNotNull(game.getPlayer("uuid"));

        game.addScore("uuid", 3, new Score(time.get(), 1000));
        assertEquals(1, game.getTopContainer().listSize(3));
        time.addAndGet(TimeUnit.DAYS.toMillis(1));
//        game.addScore("uuid", 3, new Score(time.get() - TimeUnit.DAYS.toMillis(1), 1000));
//        Thread.sleep(10);
//        assertEquals(0, game.getTopList(3).size());


    }

    @Test
    public void testPlayer() {
        int level = 3;
        String uuid = "uuid";
        Player player = new Player(uuid);
        Score score = new Score(1, 1000);
        player.addScore(level, score);
        Scores scores = player.getScores(level);
        assertTrue(scores.getBest().equal(score));

    }

    @Test
    public void testMap() {
        String uuid = "uuid";
        PlayerMap playerMap = new PlayerMap();
        assertFalse(playerMap.havePlayer(uuid));
        playerMap.addScore(uuid, 3, new Score(1, 1000));
        assertTrue(playerMap.havePlayer(uuid));
    }

    @Test
    public void testAddScore() {
        String uuid = "uuid";
        int level = 3;
        Score score = new Score(1, 1000);

        PlayerMap playerMap = new PlayerMap();
        playerMap.addScore(uuid, level, score);


    }

    @Test
    public void testTimeCorrect() {

        ArrayList<GameResult> list = new ArrayList<>();
        list.add(new GameResult(3, 50, 1000));
        list.add(new GameResult(3, 200, 2000));

        SendResult sendResult = new SendResult("uuid", 100, list);
        sendResult.timeCorrect(110);

        assertEquals(110, sendResult.sendTime);
        assertEquals(60, sendResult.list.get(0).getDateTime());
        assertEquals(110, sendResult.list.get(1).getDateTime());
    }

    @Test
    public void testPage() {
        GGame game = new GGame(new ServerTime());
        game.getPlayer("user1").setName("user1");
        game.getPlayer("user2").setName("user2");

        game.addScore("user1", 3, new Score(0, 1000));

        RPage page = game.getTopContainer().get(3).getPage(0, 1);
        assertEquals(1, page.getLength());
        assertEquals(1, page.getList().size());
        assertEquals("user1", page.getList().get(0).getName());

        game.addScore("user2", 3, new Score(0, 2000));

        game.getPlayer("user3").setName("user3");
        game.addScore("user3", 3, new Score(0, 3000));


        assertEquals(3, game.getTopContainer().get(3).getPage(0, 1).getLength());
        assertEquals(2, game.getTopContainer().get(3).getPage(0, 2).getList().size());
        assertEquals(0, game.getTopContainer().get(3).getPage(3, 2).getList().size());
    }

    @Test
    public void testUserPlace() {
        GGame game = new GGame(new ServerTime());
        game.getPlayer("user1").setName("user1");
        game.getPlayer("user2").setName("user2");
        game.getPlayer("user3").setName("user3");

        game.addScore("user1", 3, new Score(0, 1000));
        game.addScore("user2", 3, new Score(0, 2000));
        game.addScore("user3", 3, new Score(0, 3000));

        RPage page = game.getTopContainer().get(3).getPage(0, 3);
        assertEquals(3, page.getLength());
        assertEquals(3, page.getList().size());
        assertEquals("user1", page.getList().get(0).getName());
        assertEquals("user2", page.getList().get(1).getName());
        assertEquals("user3", page.getList().get(2).getName());

        assertEquals(0, game.getPlayer("user1").getScores(3).getPlace());
        assertEquals(1, game.getPlayer("user2").getScores(3).getPlace());
        assertEquals(2, game.getPlayer("user3").getScores(3).getPlace());
    }

    @Test
    public void testChatGetPage() {
        Chat chat = new Chat();
        long time = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            chat.addMessage(new ChatMessage(time + i, "Name", "Message" + i));
        }

        assertEquals(10, chat.getSize());

        ChatPage page = chat.getPage(0, 5, true);

        assertEquals(10, page.getChatSize());
        assertEquals(0, page.getFrom());
        assertEquals(5, page.getList().size());

        assertEquals("Message0", page.getList().get(0).getMessage());
        assertEquals("Message4", page.getList().get(4).getMessage());

        page = chat.getPage(1, 5, true);

        assertEquals(1, page.getFrom());
        assertEquals(5, page.getList().size());
        assertEquals("Message1", page.getList().get(0).getMessage());
        assertEquals("Message5", page.getList().get(4).getMessage());

        page = chat.getPage(9, 5, false);

        assertEquals(5, page.getFrom());
        assertEquals(5, page.getList().size());
        assertEquals("Message5", page.getList().get(0).getMessage());
        assertEquals("Message9", page.getList().get(4).getMessage());

        page = chat.getPage(8, 5, true);

        assertEquals(8, page.getFrom());
        assertEquals(2, page.getList().size());
        assertEquals("Message8", page.getList().get(0).getMessage());
        assertEquals("Message9", page.getList().get(1).getMessage());

        page = chat.getPage(1, 5, false);

        assertEquals(0, page.getFrom());
        assertEquals(2, page.getList().size());
        assertEquals("Message0", page.getList().get(0).getMessage());
        assertEquals("Message1", page.getList().get(1).getMessage());
    }

    @Test
    public void testBestScore() {
        ServerTime time = new ServerTime() {
            @Override
            public long currentTime() {
                return 1;
            }
        };
        GGame game = new GGame(time);
        Player player = game.getPlayer("uuid");
        player.setName("name");
        assertNull(player.getScores(3));
        game.addScore("uuid", 3, new Score(1, 3000));

        Player player1 = game.getPlayer("uuid1");
        player1.setName("name1");
        game.addScore("uuid1", 3, new Score(1, 2000));

        assertEquals(3000, game.getPlayer("uuid").getScores(3).getBest().getValue());

        assertEquals(1, player.getBestPlace());
        game.addScore("uuid", 4, new Score(1, 3000));
        assertEquals(0, player.getBestPlace());
        game.addScore("uuid1", 4, new Score(1, 1000));
        assertEquals(1, player.getBestPlace());
    }
}
