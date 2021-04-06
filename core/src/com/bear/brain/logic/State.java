package com.bear.brain.logic;

import com.badlogic.gdx.utils.Array;
import com.bear.brain.Brain;
import com.bear.brain.GameResult;
import com.bear.brain.events.ChangeLevel;
import com.bear.brain.net.NetListeners;
import com.bear.brain.resources.Sounds;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class State {

    public static final int MIN_LEVEL = 3;
    public static final int MAX_LEVEL = 5;

    public static DeviceTime deviceTime = new DeviceTime();

    private int size;
    private int cellsCount;

    private int next;

    private long timeStart;
    private long timer;

    public TimeLog timeLog = new TimeLog();
    public DataToServer dataToServer = new DataToServer();
    //public ResultsLog resultsLog = new ResultsLog();

    List<Cell> cells = new ArrayList<>();
    Array<StateListener> stateListeners = new Array<>();

    public ValRange valRange = new ValRange(this);
    public PressTimer pressTimer = new PressTimer(this);
    public NetListeners netListeners = new NetListeners();

    public State(int size) {
        setSize(size);
        next = 1;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        cellsCount = size * size;
    }

    public int getCellsCount() {
        return cellsCount;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public void press(int id) {
        if (id == next) {
            pressTimer.press(id);
            if (id > 1) cells.get(id - 2).setBack(true);
            cells.get(id - 1).setBack(false);
            checkStartStop(id);
            setNext(getNextId(id));
            for (StateListener listener : stateListeners) listener.press(id);
        }
    }

    private int getNextId(int currentId) {
        return currentId < cellsCount ? currentId + 1 : 1;
    }

    private void setNext(int value) {
        next = value;
    }

    public int getNext() {
        return next;
    }

    private void checkStartStop(int current) {
        if (current == 1 && !isGameStarted()) {
            start();
        }
        if (current == cellsCount) {
            stop();
        }
    }

    public long timerValue() {
        return timeStart != 0 ? deviceTime.getTime() - timeStart : timer;
    }

    private void start() {
        timer = 0;
        timeStart = deviceTime.getTime();
        pressTimer.start();
        for (StateListener listener : stateListeners) listener.start();
    }

    private void stop() {
        timer = deviceTime.getTime() - timeStart;

        GameResult gameResult = new GameResult(size, deviceTime.getTime(), timer);

        timeLog.addGameResult(size, timer);
        timeLog.removeOld(size);

        valRange.updateMinMax();

        pressTimer.stop();

        timeStart = 0;
        generateIds();
        Sounds.FINISH.play(0.5f);
        for (StateListener listener : stateListeners) listener.finish(gameResult);
    }

    public void generateIds() {
        Collections.shuffle(cells);
        for (int i = 0; i < cells.size(); i++) cells.get(i).setId(i + 1);
    }

    public void startGame() {
        if (!isGameStarted()) {
            start();
        }
    }

    public void stopGame() {
        timeStart = 0;
        setLastValueToTimer();
        generateIds();
        setNext(1);
        pressTimer.changeLevel(getSize());
        for (StateListener listener : stateListeners) listener.finish(new GameResult());
    }

    public void selectLevel(int level) {
        setSize(level);
        timeLog.removeOld(size);
        valRange.updateMinMax();
        cells.clear();
        pressTimer.changeLevel(level);
        for (StateListener listener : stateListeners) listener.changeLevel(level);
        EventBus.getDefault().post(new ChangeLevel(level));

        setLastValueToTimer();
        timeStart = 0;
        next = 1;
        generateIds();
        for (StateListener listener : stateListeners) listener.finish(new GameResult());


    }

    public void addStateListener(StateListener listener) {
        stateListeners.add(listener);
    }

    public void removeStateListener(StateListener listener) {
        stateListeners.removeValue(listener, false);
    }

    public GameResult getGameResult(int position) {
        return timeLog.getGameResult(size, position);
    }

    public int getResultsCount() {
        return timeLog.gamesCount(size);
    }

    public void reset() {
        setSize(3);
        timeLog.reset();
        dataToServer.reset();
        setLastValueToTimer();
        valRange.updateMinMax();
        Brain.player.reset();
        //resultsLog.reset();
        for (StateListener listener : stateListeners) listener.finish(new GameResult());
    }

    public void setLastValueToTimer() {
        if (getResultsCount() > 0) {
            timer = getGameResult(getResultsCount() - 1).getDuration();
        } else {
            timer = 0;
        }
    }

    public boolean isGameStarted() {
        return timeStart > 0;
    }

    public void act() {
        if (isGameStarted()) {
            pressTimer.act();
        }
    }

    public long getLastTime() {
        int results = getResultsCount();
        return results > 0 ? getGameResult(results - 1).getDuration() : 0;
    }
}
