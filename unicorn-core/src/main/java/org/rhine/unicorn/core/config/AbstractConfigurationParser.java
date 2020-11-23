package org.rhine.unicorn.core.config;

import org.rhine.unicorn.core.common.CollectionUtils;
import org.rhine.unicorn.core.common.StringUtils;
import org.rhine.unicorn.core.extension.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public abstract class AbstractConfigurationParser implements ConfigurationParser {

    private static final Logger logger = LoggerFactory.getLogger(AbstractConfigurationParser.class);

    public static String PACKAGE_LOCATIONS = "packageLocations";

    public static String STORE_TYPE = "storeType";

    private final List<ParserHandler> parserHandlers = ExtensionLoader.getInstance(ParserHandler.class);

    @Override
    public Configuration parse(Properties properties, ParserContext parserContext) {
        Configuration configuration = new GenericConfiguration();
        configuration.setProperties(properties);
        configuration.setScanLocations(Arrays.asList(StringUtils
                .splitWithCommaSeparator(properties.getProperty(PACKAGE_LOCATIONS))));
        configuration.setStoreType(properties.getProperty(STORE_TYPE));
        parserContext.setConfiguration(configuration);
        if (CollectionUtils.isNotEmpty(parserHandlers)) {
            for (ParserHandler parserHandler : parserHandlers) {
                try {
                    parserHandler.handler(properties, parserContext);
                } catch (Exception e) {
                    logger.error("[" + parserHandler.getClass().getName() + "] handler occur error", e);
                }
            }
        }
        return configuration;
    }
}
