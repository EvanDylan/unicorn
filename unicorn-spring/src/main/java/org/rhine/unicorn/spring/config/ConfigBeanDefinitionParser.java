package org.rhine.unicorn.spring.config;

import org.rhine.unicorn.spring.context.IdempotentAnnotationBeanProcessor;
import org.rhine.unicorn.spring.context.SpringBeanContainerInitializer;
import org.rhine.unicorn.spring.utils.BeanDefinitionUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ConfigBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private static final String APPLICATION_NAME = "applicationName";

    private static final String EL_PARSE_ENGINE = "elParseEngine";

    private static final String STORE_TYPE = "storeType";

    private static final String SERIALIZATION = "serialization";

    private static final String DATASOURCE = "dataSource";


    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);
        if (element.hasAttribute(APPLICATION_NAME)) {
            builder.addPropertyValue(APPLICATION_NAME, element.getAttribute(APPLICATION_NAME));
        }
        if (element.hasAttribute(EL_PARSE_ENGINE)) {
            builder.addPropertyValue(EL_PARSE_ENGINE, element.getAttribute(EL_PARSE_ENGINE));
        }
        if (element.hasAttribute(STORE_TYPE)) {
            builder.addPropertyValue(STORE_TYPE, element.getAttribute(STORE_TYPE));
        }
        if (element.hasAttribute(SERIALIZATION)) {
            builder.addPropertyValue(SERIALIZATION, element.getAttribute(SERIALIZATION));
        }
        if (element.hasAttribute(DATASOURCE)) {
            builder.addAutowiredProperty(DATASOURCE);
        }
        BeanDefinitionUtils.registerBeanDefinition(IdempotentAnnotationBeanProcessor.class, parserContext.getRegistry());
        BeanDefinitionUtils.registerBeanDefinition(SpringBeanContainerInitializer.class, parserContext.getRegistry());
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return SpringConfig.class;
    }

    @Override
    protected boolean shouldGenerateId() {
        return true;
    }
}
