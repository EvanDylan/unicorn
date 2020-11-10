package org.rhine.unicorn.core.common.io;

import java.io.InputStream;

public class Resources {

    public static InputStream getResourceAsStream(String resources) {
        if (resources == null) {
            return null;
        }
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resources);
    }

}
