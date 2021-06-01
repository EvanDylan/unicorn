package org.rhine.unicorn.core.interceptor;


import org.rhine.unicorn.core.bootstrap.Configuration;

public interface ProxyFactory {

    Object createProxy(Object targetObject, Configuration configuration);

}
