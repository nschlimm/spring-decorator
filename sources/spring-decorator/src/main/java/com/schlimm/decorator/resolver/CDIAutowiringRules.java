package com.schlimm.decorator.resolver;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

public interface CDIAutowiringRules {

	boolean applyDecoratorAutowiringRules(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor);

}
