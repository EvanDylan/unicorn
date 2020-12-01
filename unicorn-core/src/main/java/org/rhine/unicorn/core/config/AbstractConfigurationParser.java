package org.rhine.unicorn.core.config;

import com.google.common.collect.Lists;
import org.rhine.unicorn.core.utils.CollectionUtils;
import org.rhine.unicorn.core.utils.StringUtils;
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

    private final List<ParserHandler> parserHandlers = Lists.newArrayList(ExtensionLoader.getInstance(ParserHandler.class));

    private Configuration configuration;

    @Override
    public void parse(Properties properties, ParserContext parserContext) {
        configuration = new GenericConfiguration();
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
    }
}
