package com.bear.brain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bear.brain.resources.Font;
import com.bear.brain.resources.Resources;
import com.bear.tests.GdxTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(GdxTestRunner.class)
public class TestApp {
    Stage stage;

    @Before
    public void setUp() throws Exception {
        Gdx.graphics = Mockito.mock(Graphics.class);
        when(Gdx.graphics.getWidth()).thenReturn(360);
        when(Gdx.graphics.getHeight()).thenReturn(640);
        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        stage = new Stage(new ScreenViewport(), batch);

        Resources.load();

//        Gdx.app.log(TAG, "start app version " + Version.VERSION);
//        Settings.load();
//        Texts.init();
//        State.deviceTime = new DeviceTime();
//        State.init(null);
//        MonsterList.load();
//        Resources.load();
    }

    @Test
    public void badlogicLogoFileExists() {
        assertTrue(Gdx.files.internal("main.png").exists());
    }


    @Test
    public void testStageSize() {
        assertEquals(360, stage.getWidth(), 0);
        assertEquals(640, stage.getHeight(), 0);
    }

    @Test
    public void testLabelWrap() {
        Label label = new Label("test\ntest", Font.RUBIK14.black());
        label.setWrap(true);
        label.setSize(10, label.getPrefHeight());
        System.out.println(label.getWidth() + " x " + label.getHeight());
        System.out.println(label.getPrefWidth() + " x " + label.getPrefHeight());

        Table table = new Table();
        Cell<Label> cell = table.add(label);

        table.pack();
        System.out.println(table.getWidth() + " x " + table.getHeight());

        cell.prefWidth(10);
        table.pack();

        System.out.println(table.getWidth() + " x " + table.getHeight());


        Label label1 = new Label("test\ntest", Font.RUBIK14.black());
        label1.setWrap(true);
        Container<Label> container = new Container<>(label1);
//        container.prefWidth(10);
//        container.pack();
        System.out.println(container.getWidth() + " x " + container.getHeight());

        label1.setText("1");
        container.prefWidth(10);
        container.pack();
        System.out.println(container.getWidth() + " x " + container.getHeight());
        label1.setText("test\ntest");
        container.pack();
        System.out.println(container.getWidth() + " x " + container.getHeight());

    }

    @Test
    public void testLabelWrapWidth() {

    }

    @Test
    public void testTextArea() {
        TextArea textArea = new TextArea(null, new TextField.TextFieldStyle(Font.RUBIK14.font(), Color.BLACK,
                null, null, null));

        assertEquals(0, textArea.getLines());
        assertFalse(textArea.newLineAtEnd());

        System.out.println(textArea.getLinesShowing());
        System.out.println(textArea.getFirstLineShowing());
        System.out.println(textArea.getLines());
        System.out.println(textArea.getCursorLine());
        System.out.println(textArea.getPrefHeight());

    }
}
