package org.rhine.unicorn.core.config;

import com.google.common.collect.Lists;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public abstract class AbstractConfigParser implements ConfigParser {

    private static final Logger logger = LoggerFactory.getLogger(AbstractConfigParser.class);

    public static String PACKAGE_LOCATIONS = "packageLocations";

    public static String STORE_TYPE = "storeType";

    private final List<ParserHandler> parserHandlers = Lists.newArrayList(ExtensionFactory.getAllInstance(ParserHandler.class));

    @Override
    public void parse(Properties properties, ParserContext parserContext) {
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
