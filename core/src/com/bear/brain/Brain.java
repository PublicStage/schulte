package com.bear.brain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.bear.brain.help.PopupFragment;
import com.bear.brain.logic.Player;
import com.bear.brain.logic.ServerState;
import com.bear.brain.logic.State;
import com.bear.brain.menu.Menu;
import com.bear.brain.net.HttpRequest;
import com.bear.brain.resources.Resources;
import com.bear.brain.resources.Sounds;
import com.bear.brain.utils.AlarmSender;
import com.bear.brain.utils.GoToMarket;
import com.bear.brain.utils.IntentMessage;
import com.bear.lib.App;
import com.bear.lib.Layers;

public class Brain extends App {
    public static final Color backColor = Color.valueOf("EED68C");

    public static State state;
    public static Player player = new Player();
    public static ServerState serverState = new ServerState();
    public static GoToMarket goToMarket = new GoToMarket();
    public AlarmSender alarmSender = new AlarmSender();
    public IntentMessage intentMessage = new IntentMessage();

    @Override
    public void init() {
        alarmSender.removeNotification();
        alarmSender.removeAlarms();
        Sounds.ON = Save.loadSound();
        Texts.init();
        state = new State(Save.loadLevel());
        Save.loadProgress();
        Save.loadTimings();
        //Save.loadLog();
        Save.loadUser();
        Save.loadBlock();
        state.addStateListener(state.dataToServer);
        //state.addStateListener(state.resultsLog);
        serverState.setWork(false);
        player.sendLogin();
        Resources.load();
        Resources.menu = new Menu(stage.getRoot());
        Resources.batch = batch;
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Layers.setCount(3);

        state.dataToServer.sendQueueIfHave();

        if (intentMessage.hasMessage()) {
            Resources.menu.addScreen(new PopupFragment(intentMessage.getMessage()));
        }
    }

    @Override
    public void pause() {
        if (state.isGameStarted()) {
            state.stopGame();
        }
        HttpRequest.stopAllRequests();
        Save.saveProgress();
        Save.saveUser();
        Save.saveBlock();
        //Save.saveLog();
        Save.saveTimings();
        Save.saveSound(Sounds.ON);

        alarmSender.addAlarm();
    }

    @Override
    public void resume() {
        alarmSender.removeNotification();
        alarmSender.removeAlarms();
        Save.loadProgress();
        //Save.loadLog();
    }

    @Override
    public void back() {
        Resources.menu.backScreen();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(backColor.r, backColor.g, backColor.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Resources.menu.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        Resources.dispose();
    }
}
