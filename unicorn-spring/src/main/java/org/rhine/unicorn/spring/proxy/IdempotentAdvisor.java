package org.rhine.unicorn.spring.proxy;

import org.aopalliance.aop.Advice;
import org.rhine.unicorn.core.annotation.Idempotent;
import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

public class IdempotentAdvisor extends AbstractPointcutAdvisor {

    private final Set<String> packagesToScan = new LinkedHashSet<>();
    private IdempotentAnnotationInterceptor interceptor;

    public void setInterceptor(IdempotentAnnotationInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public void setPackagesToScan(Set<String> packagesToScan) {
        this.packagesToScan.addAll(packagesToScan);
    }

    @Override
    public Advice getAdvice() {
        if (interceptor == null) {
            Config config = ExtensionFactory.INSTANCE.getInstance(Config.class);
            if (config == null) {
                throw new BeanInitializationException("bean of [" + Config.class.getName() + "] is null");
            }
            interceptor = new IdempotentAnnotationInterceptor(config);
        }
        return interceptor;
    }

    @Override
    public Pointcut getPointcut() {
        return new IdempotentPointcut(packagesToScan);
    }

    @Override
    public void setOrder(int order) {
        super.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
    }

    public static class IdempotentPointcut extends StaticMethodMatcherPointcut {

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return method.isAnnotationPresent(Idempotent.class);
        }

        public IdempotentPointcut(Set<String> packagesToScan) {
            setClassFilter(new IdempotentClassFilter(packagesToScan));
        }

        public IdempotentPointcut() {
        }
    }
}
