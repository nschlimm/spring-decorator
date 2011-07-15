package com.schlimm.decorator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;

/**
 * This {@link BeanFactoryPostProcessor} sets custom {@link AutowireCandidateResolver} that ignores decorators for autowiring
 * 
 * @author Niklas Schlimm
 * 
 */
public class AlternateDecoratorAwareBeanFactoryPostProcessor implements BeanFactoryPostProcessor, Ordered {

	protected final Log logger = LogFactory.getLog(getClass());

	/*
	 * Selection strategy for the primary decorator in a chained scenario
	 */
	@SuppressWarnings("unused")
	private PrimaryBeanSelectionStrategy selectionStrategy;

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		DelegateAwareAutowireCandidateResolver newResolver = new DelegateAwareAutowireCandidateResolver();
		newResolver.setBeanFactory(beanFactory);
		((DefaultListableBeanFactory)beanFactory).setAutowireCandidateResolver(newResolver);

	}

	public void setSelectionStrategy(PrimaryBeanSelectionStrategy selectionStrategy) {
		this.selectionStrategy = selectionStrategy;
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
