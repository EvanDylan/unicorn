package org.rhine.unicorn.core.config;

import java.util.Properties;

public interface ConfigurationParser {

    void parse(Properties properties, ParserContext parserContext);

}
