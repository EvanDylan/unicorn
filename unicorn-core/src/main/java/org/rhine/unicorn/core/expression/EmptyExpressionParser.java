package org.rhine.unicorn.core.expression;

import org.rhine.unicorn.core.utils.MapUtils;
import org.rhine.unicorn.core.utils.StringUtils;

import java.util.Arrays;
import java.util.Map;

public abstract class EmptyExpressionParser implements ExpressionParser {

    private static final String EMPTY_STRING = "";

    @Override
    public Object parse(ExpressionContext context) {
        if (StringUtils.isEmpty(context.getExpression())) {
            Map<String, Object> variables = context.getVariables();
            if (MapUtils.isEmpty(variables)) {
                return EMPTY_STRING;
            } else {
                return Arrays.deepHashCode(variables.values().toArray());
            }
        }
        return doParse(context);
    }

    public abstract Object doParse(ExpressionContext context);
}
