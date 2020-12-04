package org.rhine.unicorn.core.config;

import java.util.Properties;

public interface ConfigParser {

    void parse(Properties properties, ParserContext parserContext);

}
