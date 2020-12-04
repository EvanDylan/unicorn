package org.rhine.unicorn.core.bootstrap;

import org.rhine.unicorn.core.expression.ExpressionParser;
import org.rhine.unicorn.core.expression.ExpressionParserFactory;
import org.rhine.unicorn.core.store.Store;
import org.rhine.unicorn.core.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class GenericConfiguration implements Configuration{

    private static final String STORE_TYPE_STRING = "storeType";
    private static final String SCAN_LOCATIONS_STRING = "scanLocations";
    private static final String EXPRESSION_ENGINE_STRING = "expressionEngine";

    private Properties properties;
    private String storeType;
    private List<String> scanLocations;
    private String expressionEngine;
    private ExpressionParser expressionParser;
    private Store store;
    private Scanner scanner;

    public void init() {
        storeType = getStringValue(STORE_TYPE_STRING);
        scanLocations = Arrays.asList(StringUtils
                .splitWithCommaSeparator(getStringValue(SCAN_LOCATIONS_STRING)).clone());
        expressionEngine = getStringValue(EXPRESSION_ENGINE_STRING);
        expressionParser = ExpressionParserFactory.create(expressionEngine);
    }

    @Override
    public Object getValue(String key) {
        if (properties != null) {
            return properties.get(key);
        }
        return null;
    }

    @Override
    public String getStringValue(String key) {
        if (properties != null) {
            return properties.getProperty(key);
        }
        return null;
    }

    @Override
    public Properties getProperties() {
        return this.properties;
    }

    @Override
    public String getStoreType() {
        return this.storeType;
    }

    @Override
    public List<String> getScanLocations() {
        return this.scanLocations;
    }

}
