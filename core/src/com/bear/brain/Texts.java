package com.bear.brain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public enum Texts {
    YOUR_RATING("yourRating"),
    INPUT_YOUR_NAME("inputYourName"),
    THE_LENGTH("theLength"),
    SERVER_ERROR("serverError"),
    MESSAGE("message"),
    CLICK_ON_EACH_NUMBER("clickOnEachNumber"),
    SERVER_NOT_AVAILABLE("serverNotAvailable"),
    HELP("help"),
    NEED_UPDATE("needUpdate"),
    UPDATE("update"),
    DUPLICATE("duplicate"),
    PUSH_TITLE("pushTitle"),
    PUSH1("push1"),
    PUSH2("push2"),
    PUSH3("push3"),
    PUSH4("push4"),
    OK("ok");

    public static String language;
    public static boolean isRU = false;
    private static I18NBundle bundle;
    String key;

    Texts(String key) {
        this.key = key;
    }

    public static String[] pushMessages;

    public static CharSequence formatKey(String key, Object... args) {
        return bundle.format(key, args);
    }

    public static void init() {
        language = Locale.getDefault().getLanguage();

        if ("ru".equals(language) || "uk".equals(language) || "be".equals(language) || "uz".equals(language) || "kk".equals(language)
                || "az".equals(language) || "hy".equals(language) || "ka".equals(language) || "ky".equals(language)
                || "tg".equals(language) || "tk".equals(language) || "mo".equals(language)) {
            language = "ru";
            isRU = true;
        } else {
            language = "en";
        }

        init(Gdx.files.internal("i18n/data"), language);

        pushMessages = new String[]{PUSH1.toString(), PUSH2.toString(), PUSH3.toString(), PUSH4.toString()};
    }

    public static void init(FileHandle fileHandle, String language) {
        try {
            bundle = I18NBundle.createBundle(fileHandle, new Locale(language));
            I18NBundle.setExceptionOnMissingKey(false);

        } catch (Exception ignored) {
        }
    }

    public static String get(String key) {
        return bundle.get(key);
    }

    @Override
    public String toString() {
        return bundle.get(key);
    }

    public String format(Object... args) {
        return bundle.format(key, args);
    }

    public static String getLanguage() {
        return language;
    }
}
