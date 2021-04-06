package com.bear.brain.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bear.brain.Brain;
import com.bear.brain.Save;
import com.bear.brain.Version;
import com.bear.brain.net.HttpRequest;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.concurrent.atomic.AtomicLong;

import static com.bear.brain.resources.Resources.TAG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(GdxTestRunner.class)
public class TestApp {
    Stage stage;
    AtomicLong time = new AtomicLong();

    @Before
    public void setUp() throws Exception {
        Gdx.graphics = Mockito.mock(Graphics.class);
        when(Gdx.graphics.getWidth()).thenReturn(360);
        when(Gdx.graphics.getHeight()).thenReturn(640);
        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        stage = new Stage(new ScreenViewport(), batch);

        Gdx.app.log(TAG, "start app version " + Version.VERSION + " (Gdx " + com.badlogic.gdx.Version.VERSION + ")");
        State.deviceTime = new DeviceTime() {
            @Override
            public long getTime() {
                return time.get();
            }
        };
    }

    @Test
    public void testStageSize() {
        assertEquals(360, stage.getWidth(), 0);
        assertEquals(640, stage.getHeight(), 0);
    }

    @Test
    public void atlasFileExists() {
        assertTrue("Set configurations working assets directory", Gdx.files.internal("main.atlas").exists());
    }

    @Test
    public void testZero() {
        Brain brain = new Brain();
        brain.create(stage);

        Brain.state.reset();
        assertEquals(3, Brain.state.getSize());
        assertEquals(0, Brain.state.getResultsCount());

/*
        long lastResult = Brain.state.getGameResult(Brain.state.getResultsCount() - 1).getDuration();
        assertEquals(0, lastResult);
*/


    }

    @Test
    public void testFirstGame() {
        Brain brain = new Brain();
        brain.create(stage);
        Brain.state.reset();
        Brain.state.selectLevel(4);
        time.set(1);
        assertFalse(Brain.state.isGameStarted());
        assertEquals(0, Brain.state.getResultsCount());
        assertEquals(0, Brain.state.getLastTime());
        addTestCells(Brain.state.getCellsCount());
        Brain.state.startGame();
        for (int key = 1; key <= Brain.state.getCellsCount(); key++) {
            time.addAndGet(1);
            assertTrue(Brain.state.isGameStarted());
            Brain.state.press(key);
        }
        assertFalse(Brain.state.isGameStarted());
        assertEquals(1, Brain.state.getResultsCount());
        assertEquals(16, Brain.state.getLastTime());
    }

    private void addTestCells(int cellsCount) {
        for (int i = 0; i < cellsCount; i++) {
            Brain.state.addCell(new Cell() {
                @Override
                public void setId(int value) {
                }

                @Override
                public void setBack(boolean visible) {
                }
            });
        }
    }


    @Test
    public void testSaving() {
        Save.SAVES = "test";
        Brain brain = new Brain();
        brain.create(stage);

        Brain.state.reset();
        brain.pause();

        assertEquals(3, Brain.state.getSize());
        assertEquals(0, Brain.state.getResultsCount());

        time.set(1);
        Brain.state.startGame();
        for (int key = 1; key <= 9; key++) {
            time.addAndGet(1);
            Brain.state.press(key);
        }

        HttpRequest.stopAllRequests();

        brain.pause();

        brain = new Brain();
        brain.create(stage);
        assertEquals(3, Brain.state.getSize());
        assertEquals(1, Brain.state.getResultsCount());
        assertEquals(9, Brain.state.getLastTime());

        Brain.state.reset();
        brain.pause();
    }


    @Test
    public void testSwitchLevel() {
        Brain brain = new Brain();
        brain.create(stage);
        Brain.state.reset();

        time.set(1);
        Brain.state.startGame();
        for (int key = 1; key <= 9; key++) {
            time.addAndGet(1);
            Brain.state.press(key);
        }

        for (int i = 0; i < 9; i++) {
            assertEquals(1, Brain.state.pressTimer.getTime(i));
        }

        Brain.state.selectLevel(4);
        for (int i = 0; i < 16; i++) {
            assertEquals(0, Brain.state.pressTimer.getTime(i));
        }

        Brain.state.selectLevel(3);
        for (int i = 0; i < 9; i++) {
            assertEquals(1, Brain.state.pressTimer.getTime(i));
        }

        for (int key = 1; key <= 3; key++) {
            time.addAndGet(1);
            Brain.state.press(key);
        }

        assertEquals(0, Brain.state.pressTimer.getTime(0));
        assertEquals(1, Brain.state.pressTimer.getTime(1));
        assertEquals(1, Brain.state.pressTimer.getTime(2));
        assertEquals(0, Brain.state.pressTimer.getTime(3));

    }

    @Test
    public void testSaveTiming() {
        Save.SAVES = "test";
        Brain brain = new Brain();
        brain.create(stage);
        Brain.state.reset();

        time.set(1);
        Brain.state.startGame();
        for (int key = 1; key <= 9; key++) {
            time.addAndGet(1);
            Brain.state.press(key);
        }

        for (int i = 0; i < 9; i++) {
            assertEquals(1, Brain.state.pressTimer.getTime(i));
        }

        brain.pause();

        brain = new Brain();
        brain.create(stage);

        for (int i = 0; i < 9; i++) {
            assertEquals(1, Brain.state.pressTimer.getTime(i));
        }

    }

    @Test
    public void testTimesJson() {
        Gson gson = new Gson();

        LevelsTimes levelsTimes = new LevelsTimes();

        System.out.println(gson.toJson(levelsTimes));
    }
}