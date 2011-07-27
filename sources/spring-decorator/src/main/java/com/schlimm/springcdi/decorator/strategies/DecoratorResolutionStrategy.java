package com.schlimm.springcdi.decorator.strategies;

import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * {@link DecoratorResolutionStrategy} to implement resolution logic to find decorators for the bean processed in
 * {@link DelegateAwareBeanPostProcessor}.
 * 
 * @author nschlimm
 * 
 */
public interface DecoratorResolutionStrategy {

	@SuppressWarnings("rawtypes")
	Map<String, Class> getRegisteredDecorators(ConfigurableListableBeanFactory beanFactory);
	
}
