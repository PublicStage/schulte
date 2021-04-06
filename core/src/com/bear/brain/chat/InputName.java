package com.bear.brain.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.AnswerListener;
import com.bear.brain.Brain;
import com.bear.brain.ChangeNameResult;
import com.bear.brain.RequestChangeName;
import com.bear.brain.Texts;
import com.bear.brain.events.ChangeName;
import com.bear.brain.logic.Player;
import com.bear.brain.net.HttpRequest;
import com.bear.brain.ratio.youratio.ErrorPopup;
import com.bear.brain.ratio.youratio.SendButton;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.lib.Fragment;
import com.bear.lib.S;

import org.greenrobot.eventbus.EventBus;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class InputName extends Fragment {
    Image back;
    Container<TextField> container;
    TextField textField;
    SendButton sendButton;
    ErrorPopup popup;
    boolean disableHideKeyboard;

    public InputName() {
        back = new Image(Resources.createPatch("black60"));
        addActor(back);


        NinePatch ninePatchCursor = new NinePatch(Resources.createPatch("cursor"));
        textField = new TextField(null,
                new TextField.TextFieldStyle(Font.RUBIK14.font(), Color.BLACK,
                        new NinePatchDrawable(ninePatchCursor), null, null));
        textField.setMaxLength(Player.MAX_NAME_LENGTH);
        textField.setMessageText(Texts.INPUT_YOUR_NAME.toString());
        textField.getStyle().messageFontColor = Color.BLACK;
        textField.setAlignment(Align.center);

        container = new Container<>();
        container.setActor(textField);

        container.setBackground(new NinePatchDrawable(Resources.createPatch("back_green")));
        container.fill();

        addActor(container);

        sendButton = new SendButton();
        addActor(sendButton);

        sendButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                checkNameLengthAndSend(textField.getText());
            }
        });


        back.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                Resources.menu.backScreen();
            }
        });
    }

    @Override
    public void onScreenAdded() {
        errorPopup(Texts.INPUT_YOUR_NAME.toString(), true);
        startEdit();
    }

    private void removeErrorPopup() {
        if (popup != null && Player.checkNameLength(textField.getText())) {
            popup.remove();
            popup = null;
        }
    }

    private void checkNameLengthAndSend(String name) {
        removeErrorPopup();
        disableHideKeyboardFor400ms();

        if (Player.checkNameLength(name)) {
            sendName(name);
        } else {
            errorPopup(Texts.THE_LENGTH.toString(), false);
        }
    }

    private void sendName(String name) {
        HttpRequest.request(new RequestChangeName(Brain.player.getUuid(), name), new AnswerListener<ChangeNameResult>() {
            @Override
            public void ok(ChangeNameResult answer) {

                switch (answer.getType()) {
                    case OK:
                        Brain.player.setName(answer.getName());
                        finishEdit();
                        Brain.player.setPlaces(answer.getPlaces());
                        EventBus.getDefault().post(new ChangeName(answer.getName()));
                        break;
                    case DUPLICATE:
                        errorPopup(Texts.DUPLICATE.toString(), false);
                        break;
                }
            }

            @Override
            public void cancel() {
                errorPopup(Texts.SERVER_ERROR.toString(), true);
            }
        });
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

    private void errorPopup(String text, boolean center) {
        if (popup != null) popup.remove();
        popup = new ErrorPopup(text);
        addActor(popup);
        popup.setPosition(center ? (getWidth() / 2 - S.u(40)) : S.u(16), container.getHeight());
    }

    private void startEdit() {
        getStage().setKeyboardFocus(textField);
        Gdx.input.setOnscreenKeyboardVisible(true);
    }

    private void finishEdit() {
        getStage().setKeyboardFocus(null);
        sendButton.setVisible(false);
        Gdx.input.setOnscreenKeyboardVisible(false);
        Resources.menu.backScreen();
    }


    @Override
    protected void sizeChanged() {
        back.setSize(getWidth(), getHeight());
        container.setWidth(getWidth());
        container.setSize(getWidth(), S.u(48));
        sendButton.setPosition(getWidth(), container.getHeight() / 2, Align.center | Align.right);
    }
}
