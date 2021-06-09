package org.rhine.unicorn.expression.spring;

import org.rhine.unicorn.core.expression.EmptyExpressionEngine;
import org.rhine.unicorn.core.expression.ExpressionContext;
import org.rhine.unicorn.core.extension.SPI;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

@SPI(name = "spring")
public class SpringExpressionEngine extends EmptyExpressionEngine {

    @Override
    public Object doParse(ExpressionContext context) {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        for (Map.Entry<String, Object> entry : context.getVariables().entrySet()) {
            evaluationContext.setVariable(entry.getKey(), entry.getValue());
        }
        return parser.parseExpression(context.getExpression()).getValue(evaluationContext);
    }
}
