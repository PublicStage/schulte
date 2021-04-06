package com.etheapp.brainserver;

public class Version {
    public static final int CHAT_MIN_VERSION = 1;
    public static final int RATING_MIN_VERSION = 1;

    public static String version() {
        return Version.class.getPackage().getImplementationVersion();
    }
}
