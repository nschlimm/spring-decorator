package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ReflectionUtils;

/**
 * {@link BeanPostProcessor} that evaluates delegates in the decorator and injects the subsequent bean in a chained scenario.
 * 
 * There may be multiple decorators for one bean. In such a scenario this {@link BeanPostProcessor} selects the correct bean to
 * inject into the decorator. The injected bean may be a subsequent decorator or the delegate bean instance itself.
 * 
 * Clients can implement {@link SubsequentDecoratorSelectionStrategy} to determine their own custom selection strategy for
 * subsequent decorator in a chained scenario. By default the {@link SimpleDecoratorSelectionStrategy} is used.
 * 
 * @author Niklas Schlimm
 * 
 */
public class DelegateAwareBeanPostProcessor implements BeanPostProcessor {

	@Autowired
	private DefaultListableBeanFactory beanFactory;

	@Autowired
	private SubsequentDecoratorSelectionStrategy selectionStrategy;

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean.getClass().isAnnotationPresent(Decorator.class)) {
			Field[] fields = bean.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Delegate.class)) {
					DependencyDescriptor descriptor = new DependencyDescriptor(field, true);
					String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, descriptor.getDependencyType(), true, descriptor.isEager());
					// Only autowiring candidates are relevant to this (target beans of proxies are excluded)
					List<String> autowireCandidates = new ArrayList<String>();
					for (String candidateBeanName : candidateNames) {
						if (((DefaultListableBeanFactory)beanFactory).getBeanDefinition(candidateBeanName).isAutowireCandidate()) {
							autowireCandidates.add(candidateBeanName);
						}
					}
					String beanNameOfSuccessor = selectionStrategy.findSubsequentDecoratorBeanName(beanName, autowireCandidates);
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
