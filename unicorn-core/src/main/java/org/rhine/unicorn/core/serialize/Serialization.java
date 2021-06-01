package org.rhine.unicorn.core.serialize;

public interface Serialization {

    byte id();

    <T> T deserialize(byte[] bytes, Class<T> clazz);

    <T> byte[] serialize(T object);
}
