package org.rhine.unicorn.core.config;

import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@SPI
public class DefaultConfig implements Config {

    private Properties properties;

    public Object getValue(String key) {
        if (properties != null) {
            return properties.get(key);
        }
        return null;
    }

    public String getStringValue(String key) {
        if (properties != null) {
            return properties.getProperty(key);
        }
        return null;
    }

    public String getStringValueWithDefault(String key, String defaultValue) {
        String value = getStringValue(key);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    public String getServiceName() {
        return getStringValue(SERVICE_NAME);
    }

    public String getExpressionEngineType() {
        return getStringValue(EXPRESSION_ENGINE_TYPE_STRING);
    }

    /**
     * get store type
     * @return store type
     */
    public String getStoreType() {
        return getStringValue(STORE_TYPE_STRING);
    }

    /**
     * location of will be scan
     * @return the collection of package name
     */
    public List<String> getPackageNames() {
        return Arrays.asList(StringUtils
                .splitWithCommaSeparator(getStringValue(SCAN_LOCATIONS_STRING)));
    }

    @Override
    public void setConfigProperties(Object o) {
        this.properties = (Properties) o;
    }
}
