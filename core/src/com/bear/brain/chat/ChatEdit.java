package com.bear.brain.chat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.bear.brain.AnswerListener;
import com.bear.brain.Brain;
import com.bear.brain.SendMessage;
import com.bear.brain.Texts;
import com.bear.brain.events.ChangeName;
import com.bear.brain.events.RequestChatEvent;
import com.bear.brain.net.HttpRequest;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.brain.resources.Sounds;
import com.bear.lib.S;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatEdit extends Table {
    TextArea textArea;
    ChatSendButton sendButton;

    public ChatEdit() {
        NinePatch ninePatchCursor = new NinePatch(Resources.createPatch("cursor"));

        TextField.TextFieldStyle editStyle = new TextField.TextFieldStyle(Font.RUBIK14.font(), Color.BLACK,
                new NinePatchDrawable(ninePatchCursor), null, null);
        editStyle.messageFontColor = Color.valueOf("BEAB70");

        textArea = new TextArea(null, editStyle);
        textArea.setMessageText(Texts.MESSAGE.toString());
        textArea.setPrefRows(2);

        sendButton = new ChatSendButton();

        add(textArea).padRight(S.u(4)).growX();
        add(sendButton).row();

        if (Brain.player.getName() == null) {
            textArea.setDisabled(true);
            textArea.addListener(new ActorGestureListener() {
                @Override
                public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (Brain.player.getName() == null) {
                        Resources.menu.addScreen(new InputName());
                    }
                }
            });
        }

        textArea.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if ((c == '\r' || c == '\n')) {
                    send();
                }
            }
        });

        sendButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                send();
            }
        });

        EventBus.getDefault().register(this);
    }

    private void send() {
        String text = textArea.getText().replace("\n", "").trim();
        if (text.length() > 0) {
            Sounds.FINISH.play(0.3f);
            HttpRequest.request(new SendMessage(Brain.player.getUuid(), Texts.getLanguage(), text), new AnswerListener<Object>() {
                @Override
                public void ok(Object answer) {
                    EventBus.getDefault().post(new RequestChatEvent());
                }

                @Override
                public void cancel() {

                }
            });
        }
        textArea.setText(null);
    }

    @Subscribe
    public void onMessageEvent(ChangeName event) {
        textArea.setDisabled(false);
    }

    public void dispose() {
        EventBus.getDefault().unregister(this);
    }

}
