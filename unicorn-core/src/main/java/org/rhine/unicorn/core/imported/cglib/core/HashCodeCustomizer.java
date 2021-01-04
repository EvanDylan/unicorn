package org.rhine.unicorn.core.imported.cglib.core;

import org.rhine.unicorn.core.imported.asm.Type;

public interface HashCodeCustomizer extends KeyFactoryCustomizer {
    /**
     * Customizes calculation of hashcode
     * @param e code emitter
     * @param type parameter type
     */
    boolean customize(CodeEmitter e, Type type);
}
