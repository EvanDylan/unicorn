package org.rhine.unicorn.core.config;

import java.util.List;
import java.util.Properties;

public abstract class AbstractConfigurationParser implements ConfigurationParser {

    public static String PACKAGE_LOCATIONS = "packageLocations";

    public static String STORE_TYPE = "storeType";

    private List<ParserHandler> parserHandlers;

    @Override
    public Configuration parse(Properties properties, ParserContext parserContext) {
        Configuration defaultConfiguration = new GenericConfiguration();

        String packageLocations = properties.getProperty(PACKAGE_LOCATIONS);
        String storeType = properties.getProperty(STORE_TYPE);


        return null;
    }

    public void initParserHandlers() {
        this.parserHandlers = null;
    }
}
