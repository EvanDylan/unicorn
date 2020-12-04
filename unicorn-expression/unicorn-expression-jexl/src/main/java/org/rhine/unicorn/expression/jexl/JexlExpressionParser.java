package org.rhine.unicorn.expression.jexl;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.internal.Engine;
import org.rhine.unicorn.core.expression.EmptyExpressionParser;
import org.rhine.unicorn.core.expression.ExpressionContext;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.utils.MapUtils;

import java.util.Map;

@SPI(name = "jexl")
public class JexlExpressionParser extends EmptyExpressionParser {

    @Override
    public Object doParse(ExpressionContext context) {
        if (MapUtils.isEmpty(context.getVariables())) {
            throw new IllegalArgumentException("variables can't be null or empty ");
        }
        JexlEngine engine = new Engine();
        JexlContext jexlContext = new MapContext();
        for (Map.Entry<String, Object> entry : context.getVariables().entrySet()) {
            jexlContext.set(entry.getKey(), entry.getValue());
        }
        JexlExpression expression = engine.createExpression(context.getExpression());
        return expression.evaluate(jexlContext);
    }
}
