package org.rhine.unicorn.core.annotation;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public interface DuplicateBehaviorHandler {

    Object handler(Object obj, Method method, Object[] args, MethodProxy proxy);

}
