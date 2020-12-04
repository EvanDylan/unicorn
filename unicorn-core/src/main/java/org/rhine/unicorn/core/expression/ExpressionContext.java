package org.rhine.unicorn.core.expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpressionContext {

    private Method method;

    private String expression;

    private Map<String, Object> variables = new ConcurrentHashMap<>();

    public void putVariable(String name, Object value) {
        if (name != null && value != null) {
            variables.put(name, value);
        }
    }

    public void removeVariable(String name) {
        if (name != null) {
            variables.remove(name);
        }
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "ExpressionContext{" +
                "expression='" + expression + '\'' +
                ", variables=" + variables +
                '}';
    }
}
