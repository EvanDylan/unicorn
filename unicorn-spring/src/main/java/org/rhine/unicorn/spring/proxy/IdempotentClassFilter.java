package org.rhine.unicorn.spring.proxy;

import org.rhine.unicorn.core.utils.StringUtils;
import org.springframework.aop.ClassFilter;

import java.util.LinkedHashSet;
import java.util.Set;

public class IdempotentClassFilter implements ClassFilter {

    private Set<String> packagesToScan = new LinkedHashSet<>();

    public IdempotentClassFilter() {
    }

    public IdempotentClassFilter(Set<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    @Override
    public boolean matches(Class<?> clazz) {
        if (packagesToScan.size() > 0) {
            String packageName = clazz.getPackage().getName();
            if (packagesToScan.stream().noneMatch(path -> StringUtils.contains(packageName, path))) {
                return false;
            }
        }
        return true;
    }
}
