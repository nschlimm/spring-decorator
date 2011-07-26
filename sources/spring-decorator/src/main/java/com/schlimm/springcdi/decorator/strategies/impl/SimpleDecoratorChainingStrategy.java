package com.schlimm.springcdi.decorator.strategies.impl;

import java.lang.reflect.Field;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ReflectionUtils;

import com.schlimm.springcdi.decorator.DecoratorAwareBeanFactoryPostProcessorException;
import com.schlimm.springcdi.decorator.strategies.DecoratorChainingStrategy;
import com.schlimm.springcdi.model.QualifiedDecoratorChain;

public class SimpleDecoratorChainingStrategy implements DecoratorChainingStrategy {

	public Object getChainedDecorators(ConfigurableListableBeanFactory beanFactory, QualifiedDecoratorChain chain, Object delegate) {
		for (int i = 0; i < chain.getDecorators().size(); i++) {
			Object predecessor = getTargetBean(beanFactory, chain.getDecorators().get(i).getDecoratorBeanDefinitionHolder().getBeanName());
			Object successor = delegate;
			if (i < chain.getDecorators().size() - 1) {
				// inject succeeding decorator
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
		if (beanFactory.containsBean("scopedTarget."+beanName)) {
			return beanFactory.getBean("scopedTarget."+beanName);
		}
		return beanFactory.getBean(beanName);
	}

}
