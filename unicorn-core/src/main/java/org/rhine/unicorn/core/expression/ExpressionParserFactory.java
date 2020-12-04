package org.rhine.unicorn.core.expression;

import org.rhine.unicorn.core.extension.ExtensionFactory;

public class ExpressionParserFactory {

    public static ExpressionParser create(String name) {
        return ExtensionFactory.getInstance(ExpressionParser.class, name);
    }
}
