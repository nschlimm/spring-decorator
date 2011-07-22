package com.schlimm.springcdi.decorator.strategies;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.schlimm.springcdi.model.DecoratorInfo;

public interface DelegateResolutionStrategy {
	
	String getRegisteredDelegate(ConfigurableListableBeanFactory beanFactory, DecoratorInfo decoratorInfo);

}
