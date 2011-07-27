package com.schlimm.springcdi.decorator.strategies;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.schlimm.springcdi.decorator.model.DecoratorInfo;

/**
 * {@link DelegateResolutionStrategy} to implement resolution logic to find delegate for a given decorator.
 * 
 * @author nschlimm
 * 
 */
public interface DelegateResolutionStrategy {
	
	String getRegisteredDelegate(ConfigurableListableBeanFactory beanFactory, DecoratorInfo decoratorInfo);

}
