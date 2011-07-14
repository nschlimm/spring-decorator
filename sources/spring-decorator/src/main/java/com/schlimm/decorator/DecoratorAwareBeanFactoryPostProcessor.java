package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;

/**
 * This {@link BeanFactoryPostProcessor} selects the primary decorator to inject into client references to the delegate bean. In a
 * scenario where one bean has multiple decorators this class decides which decorator is the primary bean to inject into parent
 * beans.
 * 
 * @author Niklas Schlimm
 * 
 */
@SuppressWarnings("rawtypes")
public class DecoratorAwareBeanFactoryPostProcessor implements BeanFactoryPostProcessor, Ordered {

	protected final Log logger = LogFactory.getLog(getClass());

	/*
	 * Selection strategy for the primary decorator in a chained scenario
	 */
	private PrimaryBeanSelectionStrategy selectionStrategy;

	@SuppressWarnings("unchecked")
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		Set<Class> delegateTypes = new HashSet<Class>();
		String[] bdNames = beanFactory.getBeanDefinitionNames();
		// Process all bean definitions to find decorators (and their delegates)
		for (String name : bdNames) {
			Class beanClass = beanFactory.getType(name);
			// Only decorators have delegate fields declared. We want to determine the primary decorator for those delegates.
			if (beanClass != null && beanClass.isAnnotationPresent(Decorator.class)) {
				Field[] fields = beanClass.getDeclaredFields();
				for (Field field : fields) {
					if (field.isAnnotationPresent(Delegate.class)) {
						// Already processed delegate bean type, I must not do it again (may result in multiple primary beans)
						if (delegateTypes.contains(field.getType())) {
							break;
						}
						delegateTypes.add(field.getType());
						String[] matchingBeanNames = beanFactory.getBeanNamesForType(field.getType());
						// Need to only check beans that are candidates for autowiring (to cope with proxies ... scopedTarget is ommitted)
						List<String> autowireCandidates = new ArrayList<String>();
						for (String candidateBeanName : matchingBeanNames) {
							if (((DefaultListableBeanFactory)beanFactory).getBeanDefinition(candidateBeanName).isAutowireCandidate()) {
								autowireCandidates.add(candidateBeanName);
							}
						}
						if (autowireCandidates.size() > 1) {
							// Multiple beans are primary bean candidates for delegate injection point. Need to determine primary
							// bean.
							String primaryDecoratorBeanName = selectionStrategy.determineUniquePrimaryCandidate(autowireCandidates, beanFactory);
							Class primaryDecoratorBeanClass = beanFactory.getType(primaryDecoratorBeanName);
							if (!primaryDecoratorBeanClass.isAnnotationPresent(Decorator.class)) {
								throw new DelegateAwareBeanPostProcessorException("Expected decorator to be injected! Returned: " + primaryDecoratorBeanName + "=" + primaryDecoratorBeanClass);
							}
							beanFactory.getBeanDefinition(primaryDecoratorBeanName).setPrimary(true);
						}
					}
				}
			}
		}
	}

	public void setSelectionStrategy(PrimaryBeanSelectionStrategy selectionStrategy) {
		this.selectionStrategy = selectionStrategy;
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
