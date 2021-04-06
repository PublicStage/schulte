package com.bear.brain.ratio.youratio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.AnswerListener;
import com.bear.brain.Brain;
import com.bear.brain.ChangeNameResult;
import com.bear.brain.GameResult;
import com.bear.brain.RequestChangeName;
import com.bear.brain.Texts;
import com.bear.brain.events.ChangeName;
import com.bear.brain.events.ChangePlace;
import com.bear.brain.events.OnLoadUserRatioItem;
import com.bear.brain.events.ScrollToPlace;
import com.bear.brain.events.ServerNotAvailable;
import com.bear.brain.logic.Player;
import com.bear.brain.logic.StateListener;
import com.bear.brain.net.HttpRequest;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.brain.widgets.TimeLabel;
import com.bear.lib.S;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class YourRatioItem extends Table implements StateListener {
    Label place;
    Progress progress;
    TimeLabel score;
    SendButton sendButton;

    TextField textField;
    ErrorPopup popup;

    boolean disableHideKeyboard;

    public YourRatioItem() {
        setBackground(new NinePatchDrawable(Resources.createPatch("back_green")));

        NinePatch ninePatchCursor = new NinePatch(Resources.createPatch("cursor"));
        textField = new TextField(Brain.player.getName(),
                new TextField.TextFieldStyle(Font.RUBIK14.font(), Color.BLACK,
                        new NinePatchDrawable(ninePatchCursor), null, null));
        textField.setMaxLength(Player.MAX_NAME_LENGTH);
        textField.setMessageText(Texts.INPUT_YOUR_NAME.toString());
        textField.getStyle().messageFontColor = Color.BLACK;

        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                removeErrorPopup();
                switchSendButton(Player.checkNameDifference(textField.getText()));
                if (c == '\r' || c == '\n') {
                    checkNameLengthAndSend(textField.getText());
                }

            }
        });

        place = new Label("1", Font.RUBIK14.black());
        place.setAlignment(Align.center);
        progress = new Progress();
        progress.setVisible(false);
        score = new TimeLabel(Font.RUBIK14.black());
        score.setAlignment(Align.center);
        textField.setDisabled(isTextFieldDisabled());
        textField.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (textField.isDisabled() && Brain.serverState.isWork() && !Brain.player.isRatingNeedUpdate()) {
                    textField.setDisabled(false);
                    EventBus.getDefault().post(new ScrollToPlace());
                }
            }
        });

        sendButton = new SendButton();
        sendButton.setVisible(false);
        sendButton.setPosition(getWidth(), 0, Align.bottomRight);

        sendButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                checkNameLengthAndSend(textField.getText());
            }
        });


        setTableCells(false);
        setScore();
        setPlayerPlace();

        EventBus.getDefault().register(this);
    }

    private boolean isTextFieldDisabled() {
        return Brain.player.haveResultOnCurrentLevel() || !Brain.serverState.isWork() || Brain.player.isRatingNeedUpdate();
    }

    private void setTableCells(boolean sending) {
        clear();
        if (Brain.player.getName() == null && !sending) {
            textField.setAlignment(Align.center);
            add(textField).padLeft(S.u(16)).padRight(S.u(16)).grow();
        } else if (Brain.state.valRange.hasResult()) {
            textField.setAlignment(Align.left);
            add(new Stack(progress, place)).width(S.u(48)).center().padLeft(-S.u(8));
            add(textField).grow();
            score.setAlignment(Align.right);
            add(score).width(S.u(48)).right().padRight(S.u(8));
        } else {
            textField.setAlignment(Align.left);
            add(new Stack(progress, place)).width(S.u(48)).center().padLeft(-S.u(8));
            add(textField).grow();
            score.setAlignment(Align.center);
            add(score).width(S.u(48)).center().padRight(-S.u(8));
        }
        addActor(sendButton);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        sendButton.setPosition(getWidth(), 0, Align.bottomRight);
    }

    private void removeErrorPopup() {
        if (popup != null && Player.checkNameLength(textField.getText())) {
            popup.remove();
            popup = null;
        }
    }

    private void switchSendButton(boolean value) {
        sendButton.setVisible(value);
        score.setVisible(!value);
    }

    private void checkNameLengthAndSend(String name) {
        removeErrorPopup();
        disableHideKeyboardFor400ms();

        if (Player.checkNameDifference(textField.getText())) {
            if (Player.checkNameLength(name)) {
                sendName(name);
            } else {
                errorPopup(Texts.THE_LENGTH.toString());
            }
        }
    }

    private void disableHideKeyboardFor400ms() {
        startEdit();
        disableHideKeyboard = true;
        addAction(sequence(delay(0.4f), run(new Runnable() {
            @Override
            public void run() {
                disableHideKeyboard = false;
            }
        })));
    }

    private void sendName(String name) {
        setTableCells(true);
        setProgressAnimation(true);
        HttpRequest.request(new RequestChangeName(Brain.player.getUuid(), name), new AnswerListener<ChangeNameResult>() {
            @Override
            public void ok(ChangeNameResult answer) {

                switch (answer.getType()) {
                    case OK:
                        Brain.player.setName(answer.getName());
                        setYourName(answer.getName());
                        setProgressAnimation(false);
                        finishEdit();
                        if (Brain.player.haveResultOnCurrentLevel()) {
                            Brain.player.setPlaces(answer.getPlaces());
                            EventBus.getDefault().post(new ChangeName(answer.getName()));
                        } else {
                            Brain.player.setPlaces(answer.getPlaces());
                            EventBus.getDefault().post(new ScrollToPlace());
                        }
                        break;
                    case DUPLICATE:
                        errorPopup(Texts.DUPLICATE.toString());
                        setProgressAnimation(false);
                        break;
                }


            }

            @Override
            public void cancel() {
                setProgressAnimation(false);
                errorPopup(Texts.SERVER_ERROR.toString());
            }
        });
    }

    @Subscribe
    public void onMessageEvent(ChangePlace event) {
        setPlayerPlace();
    }

    @Subscribe
    public void onMessageEvent(OnLoadUserRatioItem event) {
        textField.setDisabled(false);
    }

    @Subscribe
    public void onMessageEvent(ServerNotAvailable event) {
        textField.setDisabled(true);
    }

    public void setPlayerPlace() {
        if (Brain.player.haveResultOnCurrentLevel()) {
            //place.setVisible(true);
            place.setText(Brain.player.getPlaceOnCurrentLevel());
        } else {
            //place.setVisible(false);
            place.setText("...");
        }
        setTableCells(false);
    }

    private void setProgressAnimation(boolean value) {
        place.setVisible(!value);
        progress.setVisible(value);
        switchSendButton(!value && Player.checkNameDifference(textField.getText()));
    }

    private void errorPopup(String text) {
        if (popup != null) popup.remove();
        popup = new ErrorPopup(text);
        addActor(popup);
        popup.setPosition(0, getHeight());
    }

    private void startEdit() {
        getStage().setKeyboardFocus(textField);
        Gdx.input.setOnscreenKeyboardVisible(true);
    }

    private void finishEdit() {
        getStage().setKeyboardFocus(null);
        switchSendButton(false);
        Gdx.input.setOnscreenKeyboardVisible(false);
    }


    public void setScore() {
        if (Brain.state.valRange.hasResult()) {
            score.setTime_ss_SS(Brain.state.valRange.getMin());
        } else {
            score.setText("...");
        }
    }

    public void setYourName(String name) {
        textField.setText(name);
    }

    public void dispose() {
        Gdx.input.setOnscreenKeyboardVisible(false);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void finish(GameResult result) {

    }

    @Override
    public void press(int id) {

    }

    @Override
    public void changeLevel(int level) {
        setScore();
        setPlayerPlace();
        finishEdit();
        textField.setDisabled(isTextFieldDisabled());
    }
}
