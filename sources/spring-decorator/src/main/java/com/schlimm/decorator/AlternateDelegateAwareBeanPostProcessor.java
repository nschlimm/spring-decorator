package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;

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
public class AlternateDelegateAwareBeanPostProcessor implements BeanPostProcessor {

	@Autowired
	private DefaultListableBeanFactory beanFactory;

	@Autowired
	private DecorationStrategy decorationStrategy;

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		String[] bdNames = beanFactory.getBeanDefinitionNames();
		SortedMap<String, Field> resolvedDecorators = new TreeMap<String, Field>();
		if (AnnotationUtils.findAnnotation(bean.getClass(), Decorator.class) == null) {
			for (String bdName : bdNames) {
				Class decoratorClazz = beanFactory.getType(bdName);
				if (decoratorClazz.isAnnotationPresent(Decorator.class)) {
					Field[] decoratorClazzFields = decoratorClazz.getDeclaredFields();
					for (Field decoratorClazzField : decoratorClazzFields) {
						if (decoratorClazzField.isAnnotationPresent(Delegate.class) && beanFactory.isTypeMatch(beanName, decoratorClazzField.getType())) {
							if (beanFactory.isTypeMatch(bdName, decoratorClazzField.getType())) {
								DependencyDescriptor descriptor = new DependencyDescriptor(decoratorClazzField, true);
								BeanDefinitionHolder holder = new BeanDefinitionHolder(beanFactory.getBeanDefinition(bdName), bdName);
								if (((DelegateAwareAutowireCandidateResolver) beanFactory.getAutowireCandidateResolver()).isAutowireCandidate2(holder, descriptor)) {
									resolvedDecorators.put(bdName, decoratorClazzField);
								}
							}
						}
					}
				}
			}
		}
		if (resolvedDecorators.size() > 0) {
			return decorationStrategy.decorateDelegate(bean, resolvedDecorators);
		} else {
			return bean;
		}
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
