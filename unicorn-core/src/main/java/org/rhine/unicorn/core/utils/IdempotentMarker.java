package org.rhine.unicorn.core.utils;

public class IdempotentMarker {

    private static final ThreadLocal<Integer> TAG = new ThreadLocal<>();

    private static final int SUCCESS = 0;

    private static final int FAILED = 1;

    public static void markerSuccess() {
        if (TAG.get() == null) {
            TAG.set(SUCCESS);
        }
    }

    public static void markerFailed() {
        TAG.set(FAILED);
    }

    public static boolean isSuccess() {
        return TAG.get() == null || TAG.get() == SUCCESS;
    }
}
