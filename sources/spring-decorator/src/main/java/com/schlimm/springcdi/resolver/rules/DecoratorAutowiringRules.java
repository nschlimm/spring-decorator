package com.schlimm.springcdi.resolver.rules;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

import com.schlimm.springcdi.SpringCDIPlugin;

public interface DecoratorAutowiringRules extends SpringCDIPlugin {

	boolean applyDecoratorAutowiringRules(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor);

}
