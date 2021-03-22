package org.rhine.unicorn.core.serialize;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.rhine.unicorn.core.extension.SPI;

@SPI(name = "protostuff")
public class ProtostuffSerialization implements Serialization {

    @Override
    public String contentType() {
        return "protostuff";
    }

    @Override
    public <T> byte[] serialize(T object) {
        Schema schema = RuntimeSchema.getSchema(object.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(512);
        final byte[] protostuff;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(object, schema, buffer);
        } finally {
            buffer.clear();
        }
        return protostuff;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema schema = RuntimeSchema.getSchema(clazz);
        T t = (T) schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, t, schema);
        return t;
    }
}
