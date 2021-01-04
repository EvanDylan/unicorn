package org.rhine.unicorn.core.expression;

import org.rhine.unicorn.core.meta.ClassMetadata;
import org.rhine.unicorn.core.meta.IdempotenAnnotationtMetadata;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpressionContext {

    private final Method method;

    private final Object[] args;

    private final String expression;

    private final IdempotenAnnotationtMetadata idempotenAnnotationtMetadata;

    private final Map<String, Object> variables = new ConcurrentHashMap<>();

    private final ParameterNameResolver parameterNameResolver = new ParameterNameResolver();

    public IdempotenAnnotationtMetadata getIdempotentMetadata() {
        return idempotenAnnotationtMetadata;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public String getExpression() {
        return expression;
    }

    public ExpressionContext(Method method, Object[] args, IdempotenAnnotationtMetadata idempotenAnnotationtMetadata) {
        this.method = method;
        this.args = args;
        if (args.length != 0) {
            String[] paramNames = this.parameterNameResolver.getParameterNames(method);
            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                Object value = args[i];
                variables.put(paramName, value);
            }
        }
        this.idempotenAnnotationtMetadata = idempotenAnnotationtMetadata;
        if (this.idempotenAnnotationtMetadata != null) {
            this.expression = this.idempotenAnnotationtMetadata.getKey();
        } else {
            this.expression = null;
        }
    }
}
