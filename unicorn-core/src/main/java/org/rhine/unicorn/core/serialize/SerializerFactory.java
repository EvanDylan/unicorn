package org.rhine.unicorn.core.serialize;

import org.rhine.unicorn.core.extension.ExtensionFactory;

import java.util.List;

public class SerializerFactory {

    public static Serialization getSerialization(int id) {
        List<Serialization> allSerialization = ExtensionFactory.INSTANCE.getAllInstance(Serialization.class);
        return allSerialization.stream().filter(s -> s.id() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can't not find matched serialization id value of " + id));
    }

}
