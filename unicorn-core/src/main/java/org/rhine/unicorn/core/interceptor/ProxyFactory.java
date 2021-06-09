package org.rhine.unicorn.core.interceptor;


import org.rhine.unicorn.core.config.Config;

public interface ProxyFactory {

    Object createProxy(Object targetObject, Config config);

}
