package org.rhine.unicorn.core.config;

import java.util.Properties;

public class CustomizeHandler implements ParserHandler {

    @Override
    public void handler(Properties properties, ParserContext parserContext) {
        Configuration configuration = parserContext.getConfiguration();
        System.out.println(configuration);
    }
}
