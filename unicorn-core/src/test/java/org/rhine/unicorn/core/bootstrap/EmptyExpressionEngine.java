package org.rhine.unicorn.core.bootstrap;

import org.rhine.unicorn.core.expression.ExpressionContext;
import org.rhine.unicorn.core.expression.ExpressionEngine;
import org.rhine.unicorn.core.extension.SPI;

@SPI(name = "empty")
public class EmptyExpressionEngine implements ExpressionEngine {
    @Override
    public Object evaluate(ExpressionContext context) {
        return null;
    }
}
