package com.bear.brain;

public class Version {
    public static final int VERSION = 2;


    public static boolean needUpdate(int version) {
        return version > VERSION;
    }

}