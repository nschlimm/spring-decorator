package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

/**
 * Simple implementation of {@link DecoratorResolutionStrategy} that retrieves decorators for the bean processed in
 * {@link DelegateAwareBeanPostProcessor}. This implementation is proxy sensitive, thus enables full proxy support.
 * Also supports subclassing of decorators.
 * 
 * @author Niklas Schlimm
 * 
 */
public class SimpleDecoratorResolutionStrategy implements DecoratorResolutionStrategy {

	private static final String SCOPED_TARGET = "scopedTarget.";
	private DefaultListableBeanFactory beanFactory;

	public SimpleDecoratorResolutionStrategy(DefaultListableBeanFactory beanFactory) {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}

	/**
	 * Resolves the decorators (if any) for the current bean.
	 * 
	 * @param bean
	 *            current bean processed
	 * @param beanName
	 *            name of current bean processed
	 * @return
	 */
	public SortedMap<String, Field> resolveDecorators(final Object bean, final String beanName) {
		String[] bdNames = beanFactory.getBeanDefinitionNames();
		final SortedMap<String, Field> resolvedDecorators = new TreeMap<String, Field>();
		final Set<String> callbackParameterSet = new HashSet<String>();
		// If current bean is a decorator, ignore it
		if (AnnotationUtils.findAnnotation(bean.getClass(), Decorator.class) == null) {
			for (String bdName : bdNames) {
				// Scoped targets are ignored since the proxy object should be the decorator instance
				if (bdName.startsWith(SCOPED_TARGET))
					continue;
				callbackParameterSet.add(bdName);
				// If type of iterated bean definition is a decorator then search for @Delegate field and compare to current bean
				if (beanFactory.findAnnotationOnBean(bdName, Decorator.class) != null) {
					// retrieve all @Delegate fields that match the the actual bean type
					ReflectionUtils.doWithFields(beanFactory.getType(bdName), new FieldCallback() {
						@Override
						public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
							if (field.isAnnotationPresent(Delegate.class)) {
								// pre-check if current bean could be the delegate type of this @Delegate field
								if (field.getType().isAssignableFrom(bean.getClass())) {
									DependencyDescriptor delegateDependencyDescriptor = new DependencyDescriptor(field, true);
									BeanDefinitionHolder thisBeanDefinition = new BeanDefinitionHolder(beanFactory.getBeanDefinition(beanName), beanName);
									// check if the current bean is an autowire candidate for the @Delegate field (also checks for
									// qualifiers)
									if (((DelegateAwareAutowireCandidateResolver) beanFactory.getAutowireCandidateResolver()).isAutowireCandidate2(thisBeanDefinition, delegateDependencyDescriptor)) {
										resolvedDecorators.put((String) callbackParameterSet.toArray()[0], field);
									}
								}
							}
						}
					});
				}
				callbackParameterSet.clear();
			}
		}
		return resolvedDecorators;
	}

}
