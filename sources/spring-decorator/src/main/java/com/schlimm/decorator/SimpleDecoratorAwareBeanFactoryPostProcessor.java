package com.schlimm.decorator;

import javax.decorator.Decorator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

@SuppressWarnings("rawtypes")
public class SimpleDecoratorAwareBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	protected final Log logger = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		String[] beanNames = beanFactory.getBeanDefinitionNames();
		for (String curName : beanNames) {
			BeanDefinition bd = beanFactory.getBeanDefinition(curName);
			Class myDecorator = beanFactory.getType(curName);
			if (myDecorator.isAnnotationPresent(Decorator.class)) {
				bd.setPrimary(true);
			}
		}
	}

}
