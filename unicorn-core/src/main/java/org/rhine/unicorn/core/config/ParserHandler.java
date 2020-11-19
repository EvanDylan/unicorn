package org.rhine.unicorn.core.config;

import java.util.Properties;

public interface ParserHandler {

    Configuration handler(Properties properties, ParserContext parserContext);

}
