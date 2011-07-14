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

@SuppressWarnings("rawtypes")
public class DecoratorAwareBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	protected final Log logger = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		Set<Class> delegateTypes = new HashSet<Class>();
		String[] bdNames = beanFactory.getBeanDefinitionNames();
		for (String name : bdNames) {
			Class beanClass = beanFactory.getType(name);
			if (beanClass.isAnnotationPresent(Decorator.class)) {
				Field[] fields = beanClass.getDeclaredFields();
				for (Field field : fields) {
					if (field.isAnnotationPresent(Delegate.class)) {
						if (delegateTypes.contains(field.getType())) {
							break;
						}
						delegateTypes.add(field.getType());
						String[] matchingBeanNames = beanFactory.getBeanNamesForType(field.getType());
						for (String matchingBeanName : matchingBeanNames) {
							Class matchingBeanClass = beanFactory.getType(matchingBeanName);
							if (matchingBeanClass.isAnnotationPresent(Decorator.class)) {
								beanFactory.getBeanDefinition(matchingBeanName).setPrimary(true);
								break;
							}
						}
					}
				}
				delegateTypes.clear();
			}
		}

	}

}
