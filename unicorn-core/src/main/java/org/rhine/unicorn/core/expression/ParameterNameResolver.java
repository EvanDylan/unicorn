package org.rhine.unicorn.core.expression;

import com.google.common.collect.Maps;
import org.rhine.unicorn.core.imported.asm.*;
import org.rhine.unicorn.core.utils.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

public class ParameterNameResolver {

    private Map<Method, String[]> methodParameterNamesCache = Maps.newConcurrentMap();

    public String[] getParameterNames(Method method) {
        ClassReader classReader = ClassUtils.getClassReader(method.getDeclaringClass());
        classReader.accept(new ParameterNameResolverVisitor(method, methodParameterNamesCache), Opcodes.ASM7);
        return methodParameterNamesCache.get(method);
    }

    private static class ParameterNameResolverVisitor extends ClassVisitor {

        private final Method method;
        private final Map<Method, String[]> methodParameterNamesCache;

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (!method.getName().equals(name) || !Type.getMethodDescriptor(this.method).equals(descriptor)) {
                return methodVisitor;
            }
            String[] parameterNames = new String[this.method.getParameterCount()];
            this.methodParameterNamesCache.put(method, parameterNames);
            return new LocalVariableVisit(methodVisitor, parameterNames, Modifier.isStatic(this.method.getModifiers()));
        }

        public ParameterNameResolverVisitor(Method method, Map<Method, String[]> methodParameterNamesCache) {
            super(Opcodes.ASM7);
            this.method = method;
            this.methodParameterNamesCache = methodParameterNamesCache;
        }
    }

    private static class LocalVariableVisit extends MethodVisitor {

        private final boolean staticMethod;
        private final String[] parameterNames;

        @Override
        public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
            index = staticMethod ? index : index - 1;
            if (index != -1 && index <= parameterNames.length - 1) {
                parameterNames[index] = name;
            }
            super.visitLocalVariable(name, descriptor, signature, start, end, index);
        }

        public LocalVariableVisit(MethodVisitor methodVisitor, String[] parameterNames, boolean staticMethod) {
            super(Opcodes.ASM7, methodVisitor);
            this.parameterNames = parameterNames;
            this.staticMethod = staticMethod;
        }
    }
}
