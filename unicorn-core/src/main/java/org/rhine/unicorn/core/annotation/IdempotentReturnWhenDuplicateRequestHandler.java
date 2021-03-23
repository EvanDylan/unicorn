package org.rhine.unicorn.core.annotation;

import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.imported.cglib.proxy.MethodProxy;
import org.rhine.unicorn.core.serialize.Serialization;
import org.rhine.unicorn.core.store.Record;
import org.rhine.unicorn.core.utils.ClassUtils;
import org.rhine.unicorn.core.utils.ReflectUtils;

import java.lang.reflect.Method;

@SPI
public class IdempotentReturnWhenDuplicateRequestHandler implements DuplicateRequestHandler {

    private Serialization serialization = ExtensionFactory.INSTANCE.getInstance(Serialization.class);

    @Override
    public Object handler(Object obj, Method method, Object[] args, MethodProxy proxy, Record record) {
        if (ReflectUtils.voidReturnType(method)) {
            return Void.TYPE;
        }
        Class<?> clazz = ClassUtils.loadClass(record.getClassName());
        return serialization.deserialize(record.getResponse(), clazz);
    }

}
