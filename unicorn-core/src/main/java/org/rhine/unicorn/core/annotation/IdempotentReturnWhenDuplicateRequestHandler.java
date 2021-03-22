package org.rhine.unicorn.core.annotation;

import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.imported.cglib.proxy.MethodProxy;
import org.rhine.unicorn.core.store.Record;
import org.rhine.unicorn.core.utils.ReflectUtils;

import java.lang.reflect.Method;

@SPI
public class IdempotentReturnWhenDuplicateRequestHandler implements DuplicateRequestHandler {

    @Override
    public Object handler(Object obj, Method method, Object[] args, MethodProxy proxy, Record record) {
        if (ReflectUtils.voidReturnType(method)) {

        }

        return null;
    }

}
