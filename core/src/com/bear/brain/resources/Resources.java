package com.bear.brain.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bear.brain.menu.Menu;
import com.bear.lib.ImageLayer;
import com.bear.lib.S;

public class Resources {
    public static final String TAG = "brainlog";
    public static Fonts fonts;
    public static Batch batch;
    public static Menu menu;
    public static AssetManager manager;
    public static TextureAtlas atlas;
    static long start;

    public static void load() {
        //Gdx.app.log(TAG, "start app");
        start = System.currentTimeMillis();
        Resources.manager = new AssetManager();
        Resources.fonts = new Fonts(Resources.manager);
        Resources.manager.load("main.atlas", TextureAtlas.class);
        Resources.fonts.load();
        Sounds.addToAssetManager(manager);
        Resources.manager.finishLoading();
        Resources.fonts.setFixedGlyphs();
        Sounds.loadFromAssetManager(manager);
        Resources.atlas = Resources.manager.get("main.atlas");
        //Gdx.app.log(TAG, "load time: " + (System.currentTimeMillis() - start) + " ms");
    }


    public static Image createPixel(String name) {
        Image image = new Image(atlas.findRegion(name));
        return image;
    }

    public static Image createImage(String name) {
        Image image = new Image(atlas.findRegion(name));
        float scale = name.endsWith("2x") ? S.PIXEL_PER_UNIT / 2f : S.PIXEL_PER_UNIT;
        image.setSize(image.getWidth() * scale, image.getHeight() * scale);
        //image.setScale(name.endsWith("2x") ? S.PIXEL_PER_UNIT / 2f : S.PIXEL_PER_UNIT);
        return image;
    }

    public static ImageLayer createImageLayer(String name) {
        ImageLayer image = new ImageLayer(atlas.findRegion(name));
        float scale = name.endsWith("2x") ? S.PIXEL_PER_UNIT / 2f : S.PIXEL_PER_UNIT;
        image.setSize(image.getWidth() * scale, image.getHeight() * scale);
        return image;
    }

    public static NinePatch createPatch(String name) {
        NinePatch ninePatch = Resources.atlas.createPatch(name);
        float scale = name.endsWith("2x") ? S.PIXEL_PER_UNIT / 2f : S.PIXEL_PER_UNIT;
        ninePatch.scale(scale, scale);
        return ninePatch;
    }

    public static NinePatch createNonScalePatch(String name) {
        return Resources.atlas.createPatch(name);
    }

    public static void dispose() {
        manager.dispose();
        menu.dispose();
        batch = null;
        manager = null;
        atlas = null;
    }
}
