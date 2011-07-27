package com.schlimm.springcdi.decorator.strategies.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ClassUtils;

import com.schlimm.springcdi.decorator.DecoratorAwareBeanFactoryPostProcessorException;
import com.schlimm.springcdi.decorator.model.DecoratorInfo;
import com.schlimm.springcdi.decorator.strategies.DecoratorResolutionStrategy;

@SuppressWarnings("rawtypes")
public class SimpleDecoratorResolutionStrategy implements DecoratorResolutionStrategy {

	private HashMap<String, Class> registeredDecoratorsCache;

	public Map<String, Class> getRegisteredDecorators(ConfigurableListableBeanFactory beanFactory) {
		Map<String, Class> definitions = new HashMap<String, Class>();
		if (registeredDecoratorsCache == null) {
			registeredDecoratorsCache = new HashMap<String, Class>();
			String[] bdNames = beanFactory.getBeanDefinitionNames();
			for (String bdName : bdNames) {
				BeanDefinition bd = beanFactory.getBeanDefinition(bdName);
				if (bd instanceof AnnotatedBeanDefinition) {
					AnnotatedBeanDefinition abd = (AnnotatedBeanDefinition) bd;
					if (DecoratorInfo.isDecorator(abd)) {
						Class decoratorClass = null;
						try {
							decoratorClass = ClassUtils.forName(abd.getBeanClassName(), this.getClass().getClassLoader());
						} catch (Exception e) {
							throw new DecoratorAwareBeanFactoryPostProcessorException("Could not find decorator class: " + abd.getBeanClassName(), e);
						} 
						if (bdName.startsWith("scopedTarget.")) {
							bd = beanFactory.getBeanDefinition(bdName.replace("scopedTarget.", ""));
						}
						if (bd.isAutowireCandidate()) {
							definitions.put(bdName.replace("scopedTarget.", ""), decoratorClass);
						} 
					}
				}
			}
		} else {
			definitions = registeredDecoratorsCache;
		}

		return definitions;
	}

}
