package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.SortedMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;

/**
 * {@link BeanPostProcessor} that scanns beans to find decorators for the current bean. It will then chain the decorators and
 * return the primary decorator that gets injected for the decorated delegate bean type.
 * 
 * Allows custom implementation of decoration strategy.
 * 
 * @author Niklas Schlimm
 * 
 */
public class DelegateAwareBeanPostProcessor implements BeanPostProcessor, InitializingBean, BeanFactoryAware {

	private DefaultListableBeanFactory beanFactory;

	private DelegateDecorationStrategy decorationStrategy;

	private DecoratorResolutionStrategy decoratorResolutionStrategy;

	public DelegateAwareBeanPostProcessor() {
		super();
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
		final SortedMap<String, Field> resolvedDecorators = decoratorResolutionStrategy.resolveDecorators(bean, beanName);
		if (resolvedDecorators.size() > 0) {
			return decorationStrategy.decorateDelegate(bean, resolvedDecorators);
		} else {
			return bean;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		if (decorationStrategy == null) {
			decorationStrategy = new SimpleDecorationStrategy(beanFactory);
		}
		
		if (decoratorResolutionStrategy == null) {
			decoratorResolutionStrategy = new SimpleDecoratorResolutionStrategy(beanFactory);
		}

	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}

	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	public void setDecorationStrategy(DelegateDecorationStrategy decorationStrategy) {
		this.decorationStrategy = decorationStrategy;
	}

	public void setDecoratorResolutionStrategy(DecoratorResolutionStrategy decoratorResolutionStrategy) {
		this.decoratorResolutionStrategy = decoratorResolutionStrategy;
	}

}
