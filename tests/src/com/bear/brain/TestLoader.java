package com.bear.brain;

import com.badlogic.gdx.Gdx;
import com.bear.brain.ratio.DualLoader;
import com.bear.tests.GdxTestRunner;
import com.etheapp.brainserver.db.DBService;
import com.etheapp.brainserver.logic.GGame;
import com.etheapp.brainserver.logic.Score;
import com.etheapp.brainserver.logic.ServerTime;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class TestLoader {
    GGame game;

    @BeforeClass
    public static void beforeClass() throws Exception {
        DBServiceOneConnection.createInstance();
    }

    @Before
    public void setUp() throws Exception {
        game = new GGame(new ServerTime() {
            @Override
            public long currentTime() {
                return 0;
            }
        });
    }

    @Test
    public void testSimpleLoad() {
        Gdx.net = new TestNet(game);
        game.reset();
        game.addScore("user1", 3, new Score(0, 1000));
        game.getPlayer("user1").setName("user1");

        DualLoader loader = new DualLoader(3);
        loader.load(0, true);

        assertEquals(1, loader.maxPosition());
        assertEquals(1, loader.getCount());
        assertEquals("user1", loader.get(0).getName());
    }

    @Test
    public void testList() {
        Gdx.net = new TestNet(game);
        game.reset();

        for (int id = 0; id < 100; id++) {
            game.getPlayer("user" + id).setName("user" + id);
            game.addScore("user" + id, 3, new Score(0, 1000 + id * 1000));
        }

        DualLoader loader = new DualLoader(3);

        assertFalse(loader.isInit());
        loader.load(50, true);
        assertTrue(loader.isInit());

        assertEquals(50, loader.fromPosition());
        assertEquals(69, loader.toPosition());


        loader.get(69);

        assertEquals(50, loader.fromPosition());
        assertEquals(89, loader.toPosition());

        loader.get(50);

        assertEquals(29, loader.fromPosition());
        assertEquals(89, loader.toPosition());


        bind(loader, 9);


    }

    @Test
    public void testBind() {
        Gdx.net = new TestNet(game);
        game.reset();

        for (int id = 0; id < 100; id++) {
            game.getPlayer("user" + id).setName("user" + id);
            game.addScore("user" + id, 3, new Score(0, 1000 + id * 1000));
        }

        DualLoader loader = new DualLoader(3);

        loader.load(50, true);

        System.out.println("loader size " + loader.maxPosition());

        bind(loader, 0);
        bind(loader, 9);
        bind(loader, 19);

        System.out.println("loader size " + loader.maxPosition());

        bind(loader, 0);

        for (int i = 1; i <= 50; i++) {
            bind(loader, -i);
        }

        for (int i = 0; i < 50; i++) {
            bind(loader, i);
        }

        bind(loader, 51);

        System.out.println("loader size " + loader.maxPosition());

    }

    private void bind(DualLoader loader, int position) {
        if (position <= loader.getCount() - loader.getZero()) {
            int place = loader.getZero() + position;
            RItem data = loader.get(place);
            System.out.println("onBindData pos=" + position + ",  place=" + place + "  " + data.getName());
        } else {
            System.out.println("ERROR position " + position);
        }

    }

    @After
    public void tearDown() {
        DBService.getInstance().clearDB();
    }

}
