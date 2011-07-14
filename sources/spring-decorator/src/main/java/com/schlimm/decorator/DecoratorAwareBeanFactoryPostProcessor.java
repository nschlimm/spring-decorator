package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * This {@link BeanFactoryPostProcessor} selects the primary decorator to inject into client references to the delegate bean. In a
 * scenario where one bean has multiple decorators this class decides which decorator is the primary bean to inject into parent
 * beans.
 * 
 * @author Niklas Schlimm
 * 
 */
@SuppressWarnings("rawtypes")
public class DecoratorAwareBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

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
			if (beanClass.isAnnotationPresent(Decorator.class)) {
				Field[] fields = beanClass.getDeclaredFields();
				for (Field field : fields) {
					if (field.isAnnotationPresent(Delegate.class)) {
						// Already processed delegate bean type, I must not do it again (may result in multiple primary beans)
						if (delegateTypes.contains(field.getType())) {
							break;
						}
						delegateTypes.add(field.getType());
						String[] matchingBeanNames = beanFactory.getBeanNamesForType(field.getType());
						if (matchingBeanNames.length > 1) {
							// Multiple beans are primary bean candidates for delegate injection point. Need to determine primary
							// bean.
							String primaryDecoratorBeanName = selectionStrategy.determineUniquePrimaryCandidate(matchingBeanNames, beanFactory);
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

}
