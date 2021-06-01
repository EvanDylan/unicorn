package org.rhine.unicorn.core.utils;

import com.sun.org.apache.bcel.internal.generic.RETURN;

public class RecordFlagUtils {

    public static final long FLAG_VOID_RETURN_TYPE = 0x1;
    public static final long FLAG_SERIALIZATION_MASK = 0x7e;

    public static boolean isVoidReturnTypeFlag(long flag) {
        return (flag & FLAG_VOID_RETURN_TYPE) == FLAG_VOID_RETURN_TYPE;
    }

    public static int getSerializationId(long flag) {
        return (int) ((flag & FLAG_SERIALIZATION_MASK) >> FLAG_VOID_RETURN_TYPE);
    }

    public static long setReturnTypeFlag(long flag) {
        return flag | FLAG_VOID_RETURN_TYPE;
    }

    public static long setSerializationFlag(long flag, int serializationId) {
        return ((flag >> FLAG_VOID_RETURN_TYPE) | (long) serializationId) << FLAG_VOID_RETURN_TYPE;
    }

    private static long setFlag(long originalValue, long flag) {
        return originalValue | flag;
    }
}
