package com.jweir.socialgraph.config.seed;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class DataSeedRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String NAME = "dataSeed";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinition beanDef = BeanDefinitionBuilder
                .genericBeanDefinition(DataSeed.class)
                .addPropertyValue("baseResourcesPath", "classpath:db/seed")
                .addPropertyValue("profile", "default")
                .getBeanDefinition();

        registry.registerBeanDefinition(NAME, beanDef);
    }

}