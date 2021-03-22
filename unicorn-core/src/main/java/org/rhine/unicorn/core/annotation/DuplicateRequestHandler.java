package org.rhine.unicorn.core.annotation;

import org.rhine.unicorn.core.imported.cglib.proxy.MethodProxy;
import org.rhine.unicorn.core.store.Record;

import java.lang.reflect.Method;

public interface DuplicateRequestHandler {

    Object handler(Object obj, Method method, Object[] args, MethodProxy proxy, Record record);

}
