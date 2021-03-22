package org.rhine.unicorn.core.serialize;

public interface Serialization {

    String contentType();

    <T> T deserialize(byte[] bytes, Class<T> clazz);

    <T> byte[] serialize(T object);
}
