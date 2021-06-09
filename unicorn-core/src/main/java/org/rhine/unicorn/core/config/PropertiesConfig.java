package org.rhine.unicorn.core.config;

import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.utils.ClassUtils;
import org.rhine.unicorn.core.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.rhine.unicorn.core.config.PropertyKeyConstants.*;

@SPI
public class PropertiesConfig implements Config {

    private static final AtomicBoolean INIT = new AtomicBoolean(false);

    private static Properties properties;

    @Override
    public  <T> T getValue(String key, Class<T> targetType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStringValue(String key) {
        if (INIT.get()) {
            init();
        }
        if (properties != null) {
            return properties.getProperty(key);
        }
        return null;
    }

    @Override
    public String getApplicationName() {
        if (INIT.get()) {
            init();
        }
        return getStringValue(PROPERTY_KEY_OF_SERVICE_NAME);
    }

    @Override
    public String getElParseEngine() {
        if (INIT.get()) {
            init();
        }
        return getStringValue(PROPERTY_KEY_OF_EXPRESSION_ENGINE_TYPE_STRING);
    }

    @Override
    public String getStoreType() {
        if (INIT.get()) {
            init();
        }
        return getStringValue(PROPERTY_KEY_OF_STORE_TYPE_STRING);
    }

    @Override
    public String getSerialization() {
        if (INIT.get()) {
            init();
        }
        return getStringValue(PROPERTY_KEY_OF_SERIALIZATION_TYPE);
    }

    @Override
    public List<String> getPackageNames() {
        if (INIT.get()) {
            init();
        }
        return Arrays.asList(StringUtils
                .splitWithCommaSeparator(getStringValue(PROPERTY_KEY_OF_SCAN_LOCATIONS_STRING)));
    }

    @Override
    public void init() {
        try {
            if (INIT.compareAndSet(false, true)) {
                InputStream is = ClassUtils.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
                if (is != null) {
                    properties.load(is);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("can't find properties config file with location [" + PROPERTY_FILE_NAME + "]");
        }
    }

    @Override
    public Object getDataSource() {
        if (INIT.get()) {
            init();
        }
        return null;
    }
}
