package org.rhine.unicorn.core.utils;

public class TimeUtils {

    public static long getNow() {
        return System.currentTimeMillis();
    }

    public static long plusMillis(long current, long plus) {
        return current + plus;
    }

}
