package org.rhine.unicorn.core.utils;

public class ProxyUtils {

    public static boolean supportJdkProxy(Class<?> clazz) {
        return clazz != null && clazz.isInterface();
    }
}
