package com.schlimm.decorator;

import java.lang.reflect.Field;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ReflectionUtils;

public class DelegateAwareBeanPostProcessor implements BeanPostProcessor {

	@Autowired
	private DefaultListableBeanFactory beanFactory;

	@Autowired
	private DecoratorChain decoratorChain;

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean.getClass().isAnnotationPresent(Decorator.class)) {
			Field[] fields = bean.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Delegate.class)) {
					DependencyDescriptor descriptor = new DependencyDescriptor(field, true);
					String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, descriptor.getDependencyType(), true, descriptor.isEager());
					String beanNameOfSuccessor = decoratorChain.findSuccessorBeanName(beanName, candidateNames);
					ReflectionUtils.makeAccessible(field);
					Object successor = beanFactory.getBean(beanNameOfSuccessor);
					try {
						field.set(bean, successor);
					} catch (Exception e) {
						throw new DelegateAwareBeanPostProcessorException("Could not inject delegate!", e);
					}
				}
			}
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
