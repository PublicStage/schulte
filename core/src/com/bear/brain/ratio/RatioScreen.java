package com.bear.brain.ratio;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;
import com.bear.brain.Brain;
import com.bear.brain.GameResult;
import com.bear.brain.Texts;
import com.bear.brain.block.NeedUpdate;
import com.bear.brain.events.ServerNotAvailable;
import com.bear.brain.logic.StateListener;
import com.bear.brain.menu.TopHeader;
import com.bear.brain.ratio.youratio.YourRatioItem;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.brain.widgets.Levels;
import com.bear.lib.BaseScreenManager;
import com.bear.lib.Fragment;
import com.bear.lib.S;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;

public class RatioScreen extends Fragment implements StateListener {
    public static final String TAG = "RatioScreen";

    TopHeader header;
    YourRatioItem yourRatioItem;
    BaseScreenManager screenManager;
    Table table;

//    FPSLabel fpsLabel;

    public RatioScreen() {
        table = new Table();
        table.setFillParent(true);
        addActor(table);
        header = new TopHeader(1);
        table.add(header).padBottom(-S.u(14)).row();
        table.add(createGroupWithScreenManager()).grow().row();
        header.toFront();

        Image barDown = new Image(Resources.createPatch("bar_down.2x"));
        barDown.setTouchable(Touchable.disabled);
        table.add(barDown).fillX().padTop(-S.u(14)).row();

        table.add(new Label(Texts.YOUR_RATING.toString(), Font.RUBIK14.black())).height(S.u(32)).align(Align.center).row();
        yourRatioItem = new YourRatioItem();
        if (Brain.player.getName() != null) {
            yourRatioItem.setYourName(Brain.player.getName());
        }
        table.add(yourRatioItem).size(S.u(360), S.u(32)).row();
        table.add().height(S.u(8)).row();

        table.add(new Levels(Brain.state)).row();
//        fpsLabel = new FPSLabel(Font.RUBIK10.font(), Color.BLACK);
//        addActor(fpsLabel);

        Brain.state.addStateListener(this);
        Brain.state.addStateListener(header);
        Brain.state.addStateListener(yourRatioItem);
        EventBus.getDefault().register(this);
    }

    private WidgetGroup createGroupWithScreenManager() {
        WidgetGroup widget = new WidgetGroup() {
            @Override
            protected void sizeChanged() {
                screenManager.resize((int) getWidth(), (int) getHeight());
            }
        };
        screenManager = new BaseScreenManager(widget);
        if (Brain.player.isRatingNeedUpdate()) {
            screenManager.addScreen(new NeedUpdate());
        } else {
            screenManager.addScreen(Brain.serverState.isWork() ? new ListFragment() : new NoServerFragment());
        }

        return widget;
    }

    @Override
    protected void sizeChanged() {
//        fpsLabel.setPosition(getWidth() - S.u(40), getHeight() - fpsLabel.getHeight());
    }

    @Override
    public void dispose() {
        EventBus.getDefault().unregister(this);
        Brain.state.removeStateListener(this);
        Brain.state.removeStateListener(header);
        Brain.state.removeStateListener(yourRatioItem);
        yourRatioItem.dispose();
        header.dispose();
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
        if (Brain.player.isRatingNeedUpdate()) {
            screenManager.addScreen(new NeedUpdate());
        } else {
            screenManager.setScreen(Brain.serverState.isWork() ? new ListFragment() : new NoServerFragment(), alpha(0), alpha(1, 0.2f));
        }
    }

    @Subscribe
    public void onMessageEvent(ServerNotAvailable event) {
        System.out.println("ServerNotAvailable");
        screenManager.addScreen(new NoServerFragment());
    }

}
