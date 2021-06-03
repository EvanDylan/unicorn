package org.rhine.unicorn.core.interceptor;

import org.rhine.unicorn.core.store.RecordLog;

import java.lang.reflect.Method;

public interface DuplicateRequestHandler {

    Object handler(Method method, Object[] args, RecordLog recordLog);

}
