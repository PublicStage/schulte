package com.bear.brain.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.bear.lib.Language;

public class Fonts {

    AssetManager manager;

    public Fonts(AssetManager manager) {
        this.manager = manager;
        addFontLoader(manager);
    }

    public void addFontLoader(AssetManager manager) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    public void load() {
        generate(Font.RUBIK14);
        manager.load(Font.RUBIK10.name, BitmapFont.class, addDigits(createParameter(Font.RUBIK10)));
        manager.load(Font.RUBIK48.name, BitmapFont.class, addDigits(createParameter(Font.RUBIK48)));
    }

    public void setFixedGlyphs() {
        Font.RUBIK48.font().setFixedWidthGlyphs("1234567890");
    }

    public void generate(Font font) {
        manager.load(font.name, BitmapFont.class, addAllCharacters(createParameter(font)));
    }

    public FreetypeFontLoader.FreeTypeFontLoaderParameter createParameter(Font font) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parameter.fontFileName = font.ttf;
        parameter.fontParameters.size = font.size;
        return parameter;
    }

    public FreetypeFontLoader.FreeTypeFontLoaderParameter addAllCharacters(FreetypeFontLoader.FreeTypeFontLoaderParameter parameter) {
        parameter.fontParameters.characters = Language.all;
        return parameter;
    }

    public FreetypeFontLoader.FreeTypeFontLoaderParameter addDigits(FreetypeFontLoader.FreeTypeFontLoaderParameter parameter) {
        parameter.fontParameters.characters = "0123456789:.Старт";
        return parameter;
    }


    public FreetypeFontLoader.FreeTypeFontLoaderParameter addBorder(FreetypeFontLoader.FreeTypeFontLoaderParameter parameter) {
        parameter.fontParameters.borderWidth = 1;
        parameter.fontParameters.borderGamma = 0.1f;
        parameter.fontParameters.borderColor = new Color(0, 0, 0, 0.7f);
        return parameter;
    }

}
