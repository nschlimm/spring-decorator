package com.schlimm.decorator;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.schlimm.decorator.resolver.DecoratorInfo;

public interface DelegateResolutionStrategy {
	
	String getRegisteredDelegate(ConfigurableListableBeanFactory beanFactory, DecoratorInfo decoratorInfo);

}
