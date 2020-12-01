package org.rhine.unicorn.core.utils;

public class StringUtils {

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static String[] split(String string, String separatorChar) {
        if (isNotEmpty(string)) {
            return string.split(separatorChar);
        }
        return new String[]{};
    }

    public static String[] splitWithCommaSeparator(String string) {
        return split(string, ",");
    }
}
