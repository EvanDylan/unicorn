package org.rhine.unicorn.core.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Properties;

@RunWith(JUnit4.class)
public class DefaultConfigurationParserTest {

    @Test
    public void testParse() {
        Properties properties = new Properties();
        properties.setProperty("packageLocations", "com");
        ParserContext parserContext = new ParserContext();
        new DefaultConfigurationParser().parse(properties, parserContext);
        System.out.println(parserContext);
    }


}