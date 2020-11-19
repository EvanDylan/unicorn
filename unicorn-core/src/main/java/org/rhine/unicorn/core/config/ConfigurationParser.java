package org.rhine.unicorn.core.config;

import java.util.Properties;

public interface ConfigurationParser {

    Configuration parse(Properties properties, ParserContext parserContext);

}
