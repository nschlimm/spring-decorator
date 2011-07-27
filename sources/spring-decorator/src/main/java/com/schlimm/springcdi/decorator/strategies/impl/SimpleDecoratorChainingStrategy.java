package com.schlimm.springcdi.decorator.strategies.impl;

import java.lang.reflect.Field;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ReflectionUtils;

import com.schlimm.springcdi.decorator.DecoratorAwareBeanFactoryPostProcessorException;
import com.schlimm.springcdi.decorator.model.QualifiedDecoratorChain;
import com.schlimm.springcdi.decorator.strategies.DecoratorChainingStrategy;

/**
 * Simple decorator chaining strategy that creates the decorator chain. Can deal with scoped proxies and AOP proxies.
 * 
 * @author Niklas Schlimm
 *
 */
public class SimpleDecoratorChainingStrategy implements DecoratorChainingStrategy {

	private static final String SCOPED_TARGET = "scopedTarget.";

	public Object getChainedDecorators(ConfigurableListableBeanFactory beanFactory, QualifiedDecoratorChain chain, Object delegate) {
		for (int i = 0; i < chain.getDecorators().size(); i++) {
			// Predecessor must be the target bean (if scoped proxy)
			Object predecessor = getTargetBean(beanFactory, chain.getDecorators().get(i).getDecoratorBeanDefinitionHolder().getBeanName());
			Object successor = delegate;
			if (i < chain.getDecorators().size() - 1) {
				// successor is not the delegate, but a succeeding decorator
				successor = beanFactory.getBean(chain.getDecorators().get(i + 1).getDecoratorBeanDefinitionHolder().getBeanName());
			}
			ReflectionUtils.makeAccessible(chain.getDecorators().get(i).getDelegateFields().get(0).getDeclaredField());
			try {
				Field delegateField = chain.getDecorators().get(i).getDelegateFields().get(0).getDeclaredField();
				delegateField.set(predecessor, successor);
			} catch (Exception e) {
				throw new DecoratorAwareBeanFactoryPostProcessorException("Could not set decorator field!", e);
			} 
		}
		return beanFactory.getBean(chain.getDecorators().get(0).getDecoratorBeanDefinitionHolder().getBeanName());
	}

	private Object getTargetBean(ConfigurableListableBeanFactory beanFactory, String beanName) {
		if (beanFactory.containsBean(SCOPED_TARGET+beanName)) {
			return beanFactory.getBean(SCOPED_TARGET+beanName);
		}
		return beanFactory.getBean(beanName);
	}

}
