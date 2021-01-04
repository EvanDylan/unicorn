package org.rhine.unicorn.core.imported.cglib.core;

import org.rhine.unicorn.core.imported.asm.Opcodes;

final class AsmApi {

    /**
     * Returns the latest stable ASM API value in {@link Opcodes}.
     */
    static int value() {
        return Opcodes.ASM7;
    }

    private AsmApi() {
    }
}
