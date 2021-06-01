package org.rhine.unicorn.core.interceptor;

import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.serialize.SerializerFactory;
import org.rhine.unicorn.core.store.Record;
import org.rhine.unicorn.core.utils.ClassUtils;
import org.rhine.unicorn.core.utils.RecordFlagUtils;
import org.rhine.unicorn.core.utils.ReflectUtils;

import java.lang.reflect.Method;

@SPI
public class IdempotentReturnWhenDuplicateRequestHandler implements DuplicateRequestHandler {

    @Override
    public Object handler(Method method, Object[] args, Record record) {
        if (ReflectUtils.voidReturnType(method)) {
            return Void.TYPE;
        }
        byte[] response = record.getResponse();
        if (response == null || response.length == 0) {
            return response;
        }
        Class<?> clazz = ClassUtils.loadClass(record.getClassName());
        int serializationId = RecordFlagUtils.getSerializationId(record.getFlag());
        return SerializerFactory.getSerialization(serializationId).deserialize(record.getResponse(), clazz);
    }
}
