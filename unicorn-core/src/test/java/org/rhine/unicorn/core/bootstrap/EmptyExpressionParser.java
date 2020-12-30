package org.rhine.unicorn.core.bootstrap;

import org.rhine.unicorn.core.expression.ExpressionContext;
import org.rhine.unicorn.core.expression.ExpressionParser;
import org.rhine.unicorn.core.extension.SPI;

@SPI(name = "empty")
public class EmptyExpressionParser implements ExpressionParser {
    @Override
    public Object parse(ExpressionContext context) {
        return null;
    }
}
