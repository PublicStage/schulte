package com.bear.brain.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bear.brain.Brain;
import com.bear.brain.block.NeedUpdate;
import com.bear.brain.events.ChatAdd;
import com.bear.brain.menu.TopHeader;
import com.bear.brain.ratio.NoServerFragment;
import com.bear.brain.resources.Resources;
import com.bear.lib.Fragment;
import com.bear.lib.RecyclerDX;
import com.bear.lib.S;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatFragment extends Fragment {
//    FPSLabel fpsLabel;

    TopHeader topHeader;

    ChatAdapter adapter;
    RecyclerDX rx;

    ChatEdit chatEdit;

    public ChatFragment() {
        Table table = new Table();
        table.setFillParent(true);
        addActor(table);

        topHeader = new TopHeader(2);
        table.add(topHeader).row();

        if (Brain.player.isChatNeedUpdate()) {
            table.add(new NeedUpdate()).padTop(-S.u(12)).grow().row();
            Image barDown = new Image(Resources.createPatch("bar_down.2x"));
            barDown.setTouchable(Touchable.disabled);
            table.add(barDown).fillX().padTop(-S.u(14)).row();
        } else if (Brain.serverState.isWork()) {
            adapter = new ChatAdapter();
            rx = new RecyclerDX(adapter);
            rx.setEmptySpaceOnTop(true);
            rx.setStartFromTop(false);
            table.add(rx).padTop(-S.u(12)).grow().row();
            table.add(new Image(Resources.createPatch("line.2x"))).growX().height(S.u(2)).padTop(S.u(8)).row();
            chatEdit = new ChatEdit();
            table.add(chatEdit).growX().pad(S.u(8)).row();

            addAction(Actions.forever(Actions.sequence(Actions.delay(4f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    adapter.check();
                }
            }))));

        } else {
            table.add(new NoServerFragment()).padTop(-S.u(12)).grow().row();
            Image barDown = new Image(Resources.createPatch("bar_down.2x"));
            barDown.setTouchable(Touchable.disabled);
            table.add(barDown).fillX().padTop(-S.u(14)).row();
        }
//        fpsLabel = new FPSLabel(Font.RUBIK10.font(), Color.BLACK);
//        addActor(fpsLabel);

        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onMessageEvent(ChatAdd event) {
        if (rx.getBottomId() == -1 || rx.getBottomId() == adapter.size() - 2) {
            rx.scrollToBack();
        } else {
            //rx.update();
        }
    }

    @Override
    protected void sizeChanged() {
//        fpsLabel.setPosition(getWidth() - S.u(40), getHeight() - fpsLabel.getHeight());
    }

    @Override
    public void dispose() {
        //clearActions();
        Gdx.input.setOnscreenKeyboardVisible(false);
        EventBus.getDefault().unregister(this);
        topHeader.dispose();
        if (chatEdit != null) {
            chatEdit.dispose();
        }
    }
}
