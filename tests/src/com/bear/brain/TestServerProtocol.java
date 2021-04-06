package com.bear.brain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.bear.brain.chat.ChatAdapter;
import com.bear.brain.chat.ChatLoader;
import com.bear.brain.chat.Item;
import com.bear.brain.net.HttpRequest;
import com.bear.brain.ratio.RatioLoader;
import com.bear.tests.GdxTestRunner;
import com.etheapp.brainserver.db.DBService;
import com.etheapp.brainserver.logic.GGame;
import com.etheapp.brainserver.logic.Score;
import com.etheapp.brainserver.logic.ServerTime;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class TestServerProtocol {
    GGame game;

    @BeforeClass
    public static void beforeClass() {
        DBServiceOneConnection.createInstance();
    }

    @Before
    public void setUp() {
        game = new GGame(new ServerTime() {
            @Override
            public long currentTime() {
                return 0;
            }
        });
    }

    @After
    public void tearDown() {
        DBService.getInstance().clearDB();
    }

    @Test
    public void testLoader() {
        Gdx.net = new TestNet(game);
        game.reset();
        game.getPlayer("user1").setName("user1");
        game.addScore("user1", 3, new Score(0, 1000));

        RatioLoader loader = new RatioLoader();
        loader.load(3, 0);

        assertEquals(1, loader.getList().size);
        assertEquals("user1", loader.getList().get(0).getName());
    }

    @Test
    public void testLogin() {
        Gdx.net = new TestNet(game);

        game.reset();
        game.getPlayer("uuid").setName("name");


        final String[] serverName = new String[1];

        HttpRequest.request(new RequestLogin("uuid", Version.VERSION), new AnswerListener<InitInfo>() {
            @Override
            public void ok(InitInfo answer) {
                serverName[0] = answer.getName();
            }

            @Override
            public void cancel() {

            }
        });

        assertEquals("name", serverName[0]);
    }

    @Test
    public void testReflection() {

        AnswerListener<Integer> listener = new AnswerListener<Integer>() {
            @Override
            public void ok(Integer answer) {

            }

            @Override
            public void cancel() {

            }
        };

        System.out.println(listener.getType().getName());
    }

    @Test
    public void testVersion() {
        Gdx.net = new TestNet(game);

        final int[] version = new int[1];

        HttpRequest.request(new RequestCommand("version"), new AnswerListener<VersionCommand>() {
            @Override
            public void ok(VersionCommand answer) {
                System.out.println("version: " + answer.value);
                version[0] = answer.value;
            }

            @Override
            public void cancel() {
            }
        });

        assertEquals(1, version[0]);
    }

    @Test
    public void testEcho() {
        Gdx.net = new TestNet(game);

        final String[] str = new String[1];
        HttpRequest.request(new RequestEcho("string 12345"), new AnswerListener<String>() {
            @Override
            public void ok(String answer) {
                System.out.println("echo: " + answer);
                str[0] = answer;
            }

            @Override
            public void cancel() {

            }
        });
        assertEquals("string 12345", str[0]);
    }

    @Test
    public void testSendResult() {
        Gdx.net = new TestNet(game);

        HttpRequest.request(new RequestChangeName("uuid", "name"), new OkChangeName());

        ArrayList<GameResult> list = new ArrayList<>();
        list.add(new GameResult(3, 50, 1000));
        list.add(new GameResult(3, 200, 2000));
        final Places[] places = new Places[1];
        HttpRequest.request(new RequestSendResult(new SendResult("uuid", 100, list)), new AnswerListener<Places>() {
            @Override
            public void ok(Places answer) {
                System.out.println("SendResult answer " + answer);
                places[0] = answer;
            }

            @Override
            public void cancel() {

            }
        });

        assertEquals("{\"places\":{\"3\":0},\"users\":{\"3\":1}}", new Gson().toJson(places[0]));

    }

    @Test
    public void testSaveResultsOnServer() {
        GGame game = new GGame(new ServerTime());
        Gdx.net = new TestNet(game);

        HttpRequest.request(new RequestChangeName("uuid", "name"), new OkChangeName());

        ArrayList<GameResult> list = new ArrayList<>();
        list.add(new GameResult(3, game.getServerTime().currentTime() + 50, 1000));
        list.add(new GameResult(3, game.getServerTime().currentTime() + 200, 2000));

        HttpRequest.request(new RequestSendResult(new SendResult("uuid", 100, list)), new AnswerListener<String>() {
            @Override
            public void ok(String answer) {
            }

            @Override
            public void cancel() {
            }
        });

        assertEquals(1, game.getTopContainer().get(3).getPage(0, 10).getLength());
        game.reset();
        assertEquals(0, game.getTopContainer().get(3).getPage(0, 10).getLength());

        game.load(DBService.getInstance().loadResults());
        assertEquals(1, game.getTopContainer().get(3).getPage(0, 10).getLength());
    }

    @Test
    public void testSaveNameOnServer() {
        GGame game = new GGame(new ServerTime());
        Gdx.net = new TestNet(game);

        ArrayList<GameResult> list = new ArrayList<>();
        list.add(new GameResult(3, game.getServerTime().currentTime() + 50, 1000));
        list.add(new GameResult(3, game.getServerTime().currentTime() + 200, 2000));
        HttpRequest.request(new RequestSendResult(new SendResult("uuid", 100, list)), new OkAnswer());


        HttpRequest.request(new RequestChangeName("uuid", "name"), new AnswerListener<ChangeNameResult>() {
            @Override
            public void ok(ChangeNameResult answer) {
                System.out.println("ChangeNameResult " + answer.getName() + " " + answer.type.toString());
            }

            @Override
            public void cancel() {
                System.out.println("RequestChangeName cancel");
            }
        });
        game.reset();
        game.load(DBService.getInstance().loadResults());


        final RPage[] rPage = new RPage[1];
        HttpRequest.request(new RequestRatioList(3, 0, true, null), new AnswerListener<RPage>() {
            @Override
            public void ok(RPage answer) {
                rPage[0] = answer;
            }

            @Override
            public void cancel() {

            }
        });

        assertEquals("name", rPage[0].getList().get(0).getName());
    }

    @Test
    public void testJson() {

        ArrayList<GameResult> list = new ArrayList<>();
        list.add(new GameResult(3, 50, 1000));
        list.add(new GameResult(3, 200, 2000));

        SendResult sendResult = new SendResult("uuid", 100, list);

        RequestSendResult command = new RequestSendResult(sendResult);

        String jsonString = new Json(JsonWriter.OutputType.json).toJson(command);
        String gsonString = new Gson().toJson(command);

        System.out.println(jsonString);
        System.out.println(gsonString);

        //RequestSendResult jsonCommand = new Json().fromJson(RequestSendResult.class, gsonString);
        RequestSendResult gsonCommand = new Gson().fromJson(gsonString, RequestSendResult.class);

        //SendResult sr1 = (SendResult) jsonCommand.parameter;
        SendResult sr2 = gsonCommand.parameter;

        System.out.println("");
    }

    @Test
    public void testSeveralUsersResults() {
        Gdx.net = new TestNet(game);
        game.reset();

        game.getPlayer("user1").setName("user1");
        game.getPlayer("user2").setName("user2");

        ArrayList<GameResult> list = new ArrayList<>();
        list.add(new GameResult(3, 50, 2000));
        list.add(new GameResult(3, 200, 3000));

        HttpRequest.request(new RequestSendResult(new SendResult("user1", 100, list)), new OkAnswer());

        list.add(new GameResult(3, 50, 1000));
        HttpRequest.request(new RequestSendResult(new SendResult("user2", 100, list)), new OkAnswer());

        RatioLoader loader = new RatioLoader();
        loader.load(3, 0);

        assertEquals(2, loader.getList().size);
        assertEquals("user2", loader.getList().get(0).getName());
        assertEquals("user1", loader.getList().get(1).getName());


    }

    @Test
    public void testDBLocalTests() {
        assertTrue(DBService.getInstance().connectionCheck());
    }

    @Test
    public void testName() {

    }

    @Test
    public void emptyNames() {
        GGame game = new GGame(new ServerTime());
        Gdx.net = new TestNet(game);
        game.reset();
        game.getPlayer("user1").setName("name1");

        ArrayList<GameResult> list = new ArrayList<>();
        list.add(new GameResult(3, 50, 1000));
        HttpRequest.request(new RequestSendResult(new SendResult("user1", 100, list)), new OkAnswer());

        assertEquals(1, game.getTopContainer().listSize(3));

        list.clear();
        list.add(new GameResult(3, 50, 2000));
        HttpRequest.request(new RequestSendResult(new SendResult("user2", 100, list)), new OkAnswer());

        assertEquals(1, game.getTopContainer().listSize(3));

        HttpRequest.request(new RequestChangeName("user2", "name2"), new OkChangeName());

        assertEquals(2, game.getTopContainer().listSize(3));
    }

    @Test
    public void testChat() {
        GGame game = new GGame(new ServerTime());
        Gdx.net = new TestNet(game);
        game.reset();
        long time = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            game.getChat("ru").addMessage(new ChatMessage(time, "Name", "Message" + i));
        }

        //HttpRequest.request(new RequestChat(-1), new OkAnswer());

        ChatLoader loader = new ChatLoader();
        loader.loadFirst();

        assertEquals("Message5", loader.get(5).getMessage());
        assertEquals("Message9", loader.get(9).getMessage());

        assertEquals(9, loader.getLastId());
        loader.loadNext();
        assertEquals(9, loader.getLastId());

        game.getChat("ru").addMessage(new ChatMessage(time, "Name", "Message" + 10));

        loader.loadNext();
        assertEquals(10, loader.getLastId());
        assertEquals("Message10", loader.get(10).getMessage());

        game.getChat("ru").addMessage(new ChatMessage(time, "Name", "Message" + 11));
        game.getChat("ru").addMessage(new ChatMessage(time, "Name", "Message" + 12));
        loader.loadNext();
        assertEquals(12, loader.getLastId());
        assertEquals("Message12", loader.get(12).getMessage());

        loader.loadPrev();

        assertEquals("Message0", loader.get(0).getMessage());
        assertEquals("Message4", loader.get(4).getMessage());
        assertEquals("Message12", loader.get(12).getMessage());
    }

    @Test
    public void testSendMessage() {
        Gdx.net = new TestNet(game);
        game.reset();
        game.getPlayer("user1").setName("user1");
        game.getPlayer("user2").setName("user2");

        ChatLoader loader = new ChatLoader();
        loader.loadFirst();

        assertEquals(0, loader.size());

        HttpRequest.request(new SendMessage("user1", "ru", "test"), new OkAnswer());

        loader.loadNext();

        assertEquals(1, loader.size());


        HttpRequest.request(new SendMessage("user1", "ru", "test test"), new OkAnswer());
        loader.loadNext();
        assertEquals(2, loader.size());


        HttpRequest.request(new SendMessage("user1", "ru", "абв"), new OkAnswer());
        loader.loadNext();
        assertEquals(3, loader.size());

    }

    @Test
    public void testRus() {
        Texts.language = "ru";
        Gdx.net = new TestNet(game);
        game.reset();
        game.getPlayer("user1").setName("user1");
        ChatLoader loader = new ChatLoader();
        loader.loadFirst();
        HttpRequest.request(new SendMessage("user1", "ru", "а"), new OkAnswer());
        loader.loadNext();
        System.out.println(loader.get(0).getMessage());
    }

    @Test
    public void testChatDates() {
        Texts.language = "ru";
        GGame game = new GGame(new ServerTime());
        Gdx.net = new TestNet(game);
        game.reset();
        long time = System.currentTimeMillis();
        game.getChat("ru").addMessage(new ChatMessage(1, "Name0", "Message0"));
        game.getChat("ru").addMessage(new ChatMessage(1, "Name1", "Message1"));


        ChatLoader loader = new ChatLoader();
        loader.loadFirst();


        assertEquals(0, loader.getZeroId());
        assertEquals(1, loader.size() - 1);

        assertEquals("Message0", loader.get(0).getMessage());
        assertEquals("Message1", loader.get(1).getMessage());

        ChatAdapter adapter = new ChatAdapter();
        assertEquals(2, adapter.size());
        assertEquals(adapter.minPosition() + 1, adapter.size() - 1);

        assertEquals(Item.class, adapter.getType(adapter.minPosition()));
        assertEquals(Item.class, adapter.getType(adapter.minPosition() + 1));

        game.reset();
        game.getChat("ru").addMessage(new ChatMessage(1, "Name0", "Message0"));
        game.getChat("ru").addMessage(new ChatMessage(2, "Name1", "Message1"));

        loader = new ChatLoader();
        loader.loadFirst();

        assertEquals(0, loader.getZeroId());
        assertEquals(1, loader.size() - 1);

        adapter = new ChatAdapter();
        assertEquals(2, adapter.size());
        assertEquals(adapter.minPosition() + 1, adapter.size() - 1);

        assertEquals(Item.class, adapter.getType(adapter.minPosition()));
        //assertEquals(ItemDate.class, adapter.getType(adapter.minPosition() + 1));
        assertEquals(Item.class, adapter.getType(adapter.minPosition() + 1));

    }

    @Test
    public void testLevelsInChat() {
        Texts.language = "ru";
        Gdx.net = new TestNet(game);
        game.reset();
        game.getPlayer("user1").setName("user1");

        ChatLoader loader = new ChatLoader();
        HttpRequest.request(new SendMessage("user1", "ru", "test"), new OkAnswer());
        loader.loadNext();
        assertEquals("user1", loader.get(0).getName());
        assertEquals(0, loader.get(0).getPlace());

        game.addScore("user1", 3, new Score(0, 1000));
        HttpRequest.request(new SendMessage("user1", "ru", "test"), new OkAnswer());
        loader.loadNext();
        assertEquals("user1", loader.get(1).getName());
        assertEquals(1, loader.get(1).getPlace());
    }
}
