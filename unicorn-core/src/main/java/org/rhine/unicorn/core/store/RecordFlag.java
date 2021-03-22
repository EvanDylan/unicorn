package org.rhine.unicorn.core.store;

public class RecordFlag {

    public static final long VOID_RETURN_TYPE_FLAG = 0x1;
    public static final long SERIALIZE__PROTOBUF_FLAG = 0x1 << 1;

    public static boolean isVoidReturnTypeFlag(long flag) {
        return (flag & VOID_RETURN_TYPE_FLAG) == VOID_RETURN_TYPE_FLAG;
    }

    public static boolean isProtoBuf(long flag) {
        return (flag & SERIALIZE__PROTOBUF_FLAG) == SERIALIZE__PROTOBUF_FLAG;
    }

    public static long settingFlag(long originalValue, long flag) {
        return originalValue | flag;
    }
}
