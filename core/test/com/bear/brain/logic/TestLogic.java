package com.bear.brain.logic;

import com.badlogic.gdx.math.Vector2;
import com.bear.brain.LevelsData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import junit.framework.TestCase;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicLong;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class TestLogic extends TestCase {

    AtomicLong time = new AtomicLong();

    @Override
    public void setUp() throws Exception {
        State.deviceTime = new DeviceTime() {
            @Override
            public long getTime() {
                return time.get();
            }
        };
    }


    public void testCoder() throws BadPaddingException, IOException, IllegalBlockSizeException {

        TimeLog timeLog = new TimeLog();
        timeLog.addGameResult(3, 1000);
        timeLog.addGameResult(3, 2000);
        timeLog.addGameResult(3, 3000);
    }

    public void testTimeLog() {
        TimeLog timeLog = new TimeLog();

        timeLog.addGameResult(3, 1000);
        assertEquals(1000, timeLog.getGameResult(3, 0).getDuration());
        timeLog.addGameResult(3, 2000);
        assertEquals(2000, timeLog.getGameResult(3, 1).getDuration());
        assertEquals(1000, timeLog.getGameResult(3, 0).getDuration());
        timeLog.addGameResult(4, 2500);
        assertEquals(2500, timeLog.getGameResult(4, 0).getDuration());

    }

    public void testVector() {
        assertEquals(0.0, new Vector2(2, 1).sub(new Vector2(1, 1)).angle(), 0);
        assertEquals(45.0, new Vector2(2, 2).sub(new Vector2(1, 1)).angle(), 0);
        assertEquals(180.0, new Vector2(0, 1).sub(new Vector2(1, 1)).angle(), 0);
        assertEquals(270.0, new Vector2(1, 0).sub(new Vector2(1, 1)).angle(), 0);
    }

    public void testShulteData() {

        LevelsData levels = new LevelsData();
        levels.add(3, 1, 1);
        levels.add(3, 2, 2);
        levels.add(3, 3, 1);
        levels.add(4, 1, 10);
        levels.add(4, 2, 20);
        levels.add(4, 3, 15);

        Gson gson = new Gson();

        String s = gson.toJson(levels);
        System.out.println(s);

        Type type = new TypeToken<LevelsData>() {
        }.getType();

        LevelsData levels1 = gson.fromJson(s, LevelsData.class);

        System.out.println(gson.toJson(levels1));
    }

    public void testPressTimer() {

        time.incrementAndGet();

        State state = new State(3);

        for (int i = 0; i < 9; i++) state.addCell(new TestCell());

        assertFalse(state.isGameStarted());
        assertEquals(1, state.getNext());
        state.startGame();

        time.addAndGet(2);

        state.press(1);
        assertTrue(state.isGameStarted());
        assertEquals(2, state.getNext());
        time.incrementAndGet();
        state.press(2);
        assertEquals(3, state.getNext());

        for (int i = 3; i <= 9; i++) {
            time.incrementAndGet();
            state.press(i);
        }

        assertEquals(2, state.pressTimer.getTime(0));
        for (int i = 1; i < state.getCellsCount(); ++i)
            assertEquals(1, state.pressTimer.getTime(i));

        for (int i = 0; i < state.getCellsCount(); ++i) {
            System.out.print(state.pressTimer.getTime(i) + " ");
        }
        System.out.println("");

        assertEquals(1000, state.pressTimer.current.maxTime);


    }

    public class TestCell implements Cell {

        @Override
        public void setId(int value) {

        }

        @Override
        public void setBack(boolean visible) {

        }
    }

    public void testResultsRemoveOld() {
        ResultInterval resultInterval = new ResultInterval();
        resultInterval.addResult(3, 5, 1);
        resultInterval.addResult(3, 4, 2);
        resultInterval.addResult(3, 6, 3);

        assertEquals(3, resultInterval.size());
        assertEquals(5, resultInterval.getResult(0).getDuration());
        resultInterval.removeOld(2);
        assertEquals(2, resultInterval.size());
        assertEquals(4, resultInterval.getResult(0).getDuration());
        assertEquals(6, resultInterval.getResult(1).getDuration());

        // можно делать проверку во время
        // добавления нового результата, проверки сервера, старта, переключения уровня
        // после проверки, если были изменения, сохранить состояние
    }

    public void testSigma() {

        State state = new State(3);
        assertEquals(0, state.getResultsCount());
        state.timeLog.addGameResult(3, 3000);
        state.timeLog.addGameResult(3, 4000);
        state.timeLog.addGameResult(3, 5000);
        state.timeLog.addGameResult(3, 6000);
        state.valRange.updateMinMax();
        //assertEquals(4000, state.valRange.getAverage());

        System.out.println("AVERAGE " + state.valRange.getAverage());
        System.out.println("SKO " + state.valRange.getSko());
        System.out.println("RANGE " + (state.valRange.getAverage() - 2 * state.valRange.getSko()) + " ... " + (state.valRange.getAverage() + 2 * state.valRange.getSko()));

        //assertEquals(1000, state.valRange.getSko());


    }

    public void testName() {
        int initLevel = 3;
        State state = new State(initLevel);
        assertEquals(0, state.getResultsCount());

        time.set(TimeLog.DAY);

        state.timeLog.loadGameResult(3, time.get() - 2, 10);
        state.timeLog.loadGameResult(3, time.get() - 1, 20);
        state.timeLog.loadGameResult(3, time.get() + TimeLog.DAY, 30);

        time.addAndGet(TimeLog.DAY);

        assertEquals(3, state.getResultsCount());
        state.timeLog.removeOld(state.getSize());
        assertEquals(1, state.getResultsCount());

    }
}
