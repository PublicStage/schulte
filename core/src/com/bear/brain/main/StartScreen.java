package com.bear.brain.main;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bear.brain.Brain;
import com.bear.brain.GameResult;
import com.bear.brain.diagram3.Diagram3;
import com.bear.brain.logic.StateListener;
import com.bear.brain.menu.TopHeader;
import com.bear.brain.process.ProcessWidget;
import com.bear.brain.resources.Resources;
import com.bear.brain.resources.Sounds;
import com.bear.brain.widgets.Levels;
import com.bear.lib.Fragment;
import com.bear.lib.S;

public class StartScreen extends Fragment implements StateListener {
    public static final String TAG = "StartScreen";

    Field field;
    //Timer timer;
    Table table;

    TopHeader topHeader;
    Diagram3 diagram;
    Image darkCaption;
    Caption caption;
    ProcessWidget processWidget;
    Image darkTopBack;
    Levels levels;
    ButtonsGroup buttons;

    float processWidgetY;

//    FPSLabel fpsLabel;

    public StartScreen() {
        table = new Table();
        table.setFillParent(true);

        field = new Field(Brain.state);
        //timer = new Timer(Brain.state);
        topHeader = new TopHeader(0);
        table.add(topHeader).row();

        darkCaption = new Image(Resources.createPatch("black24"));

        diagram = new Diagram3();
        caption = new Caption();
        caption.setCellsCount(Brain.state.getCellsCount());

        processWidget = new ProcessWidget();
        processWidget.setTouchable(Touchable.disabled);
        table.add(diagram).grow().padTop(S.u(-12)).row();

        table.swapActor(topHeader, diagram);

        levels = new Levels(Brain.state);
        Image bar = new Image(Resources.createPatch("bar_up.2x"));
        table.add(bar).fillX().padBottom(-S.u(13)).row();
        table.add(field).row();
        table.swapActor(bar, field);
        table.add(levels).height(S.u(36)).padTop(-S.u(36)).row();
        Image barDown = new Image(Resources.createPatch("bar_down.2x"));
        barDown.setTouchable(Touchable.disabled);
        table.add(barDown).fillX().padTop(-S.u(14)).row();

        addActor(darkCaption);
        addActor(table);
        addActor(caption);

        darkTopBack = new Image(Resources.createPatch("black80"));
        addActor(darkTopBack);
        darkTopBack.setVisible(false);


        addActor(processWidget);
        //processWidget.setVisible(false);

        buttons = new ButtonsGroup();
        buttons.setTouchable(Touchable.childrenOnly);

        addActor(buttons);

        Brain.state.generateIds();
        Brain.state.addStateListener(this);
        Brain.state.addStateListener(topHeader);

//        fpsLabel = new FPSLabel(Font.RUBIK10.font(), Color.BLACK);
//        addActor(fpsLabel);
    }

    @Override
    protected void sizeChanged() {
        table.pack();
        processWidgetY = field.getY() + field.getHeight() - processWidget.getHeight() - S.u(4);

        processWidget.setPosition(getWidth() / 2 - processWidget.getWidth() / 2, processWidgetY);
        Brain.state.selectLevel(Brain.state.getSize()); // TODO проинициализировать ProcessWidget красиво

        buttons.setSize(getWidth(), getHeight());

//        fpsLabel.setPosition(getWidth() - S.u(40), getHeight() - fpsLabel.getHeight());
    }

    @Override
    public void start() {
        levels.setVisible(false);
        buttons.setVisible(false);
        darkTopBack.setVisible(true);
        darkTopBack.setSize(diagram.getWidth(), diagram.getHeight());
        darkTopBack.setPosition(getWidth(), diagram.localToStageCoordinates(new Vector2()).y);
        darkTopBack.addAction(Actions.moveBy(-getWidth(), 0, 0.2f));

        if (processWidget.isVisible()) {
            float toY = diagram.localToStageCoordinates(new Vector2()).y;
            toY += (diagram.getHeight() - processWidget.getHeight()) / 2;
            processWidget.addAction(Actions.moveTo(processWidget.getX(), (int) toY, 0.2f));
        } else {
            processWidget.setVisible(true);
            processWidget.setPosition(getWidth() / 2 - processWidget.getWidth() / 2, diagram.localToStageCoordinates(new Vector2()).y);
        }
    }

    @Override
    public void finish(GameResult result) {

        levels.setVisible(true);
        buttons.setVisible(true);
        darkTopBack.addAction(Actions.moveBy(getWidth(), 0, 0.2f));

        processWidget.addAction(Actions.moveTo(processWidget.getX(), processWidgetY, 0.2f));

        setCaption();

        processWidget.setVisible(Brain.state.getResultsCount() > 0);
    }

    private void setCaption() {
        caption.setCellsCount(Brain.state.getCellsCount());
        caption.setVisible(Brain.state.getResultsCount() == 0);
        caption.setSize(diagram.getWidth(), diagram.getHeight());
        caption.setPosition(diagram.getX(), diagram.getY());

        diagram.setVisible(Brain.state.getResultsCount() > 0);

        darkCaption.setSize(caption.getWidth(), caption.getHeight() + S.u(16));
        darkCaption.setPosition(caption.getX(), caption.getY() - S.u(8));
        darkCaption.setVisible(caption.isVisible());
    }

    @Override
    public void press(int id) {
        Sounds.CLICK.play(0.3f);
    }

    @Override
    public void changeLevel(int level) {
    }

    @Override
    public boolean back() {
        if (Brain.state.isGameStarted()) {
            Brain.state.stopGame();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void dispose() {
        Brain.state.removeStateListener(this);
        Brain.state.removeStateListener(topHeader);
        topHeader.dispose();
    }
}
