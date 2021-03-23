package org.rhine.unicorn.expression.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.rhine.unicorn.core.expression.ExpressionEngine;
import org.rhine.unicorn.core.extension.ExtensionLoader;
import org.springframework.util.Assert;

@RunWith(JUnit4.class)
public class SpringExpressionEngineTest {


    @Test
    public void load() {
        Class<?> expressionParser = ExtensionLoader.loadExtensionClass(ExpressionEngine.class, "spring");
        Assert.notNull(expressionParser);
    }

}