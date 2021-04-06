package com.bear.tests;

import com.bear.lib.Utils;

public class UtilsHeadlessDesktop extends Utils {
    @Override
    public String platformName() {
        return "Tests";
    }

    @Override
    public float density() {
        return 1f;
    }

    @Override
    public int dpToPx(float dp) {
        return (int) dp;
    }
}
