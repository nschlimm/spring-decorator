package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.schlimm.decorator.resolver.DelegateAwareAutowireCandidateResolver;

/**
 * Simple implementation of {@link DecoratorResolutionStrategy} that retrieves decorators for the bean processed in
 * {@link DelegateAwareBeanPostProcessor}. This implementation is proxy sensitive, thus enables full proxy support. Also supports
 * subclassing of decorators.
 * 
 * @author Niklas Schlimm
 * 
 */
public class SimpleDecoratorResolutionStrategy implements DecoratorResolutionStrategy {

	private static final String SCOPED_TARGET = "scopedTarget.";
	private DefaultListableBeanFactory beanFactory;
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> registeredDecoratorsCache;

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
	@SuppressWarnings("rawtypes")
	public SortedMap<String, Field> resolveDecorators(final Object bean, final String beanName) {
		final SortedMap<String, Field> resolvedDecorators = new TreeMap<String, Field>();
		final Set<String> callbackParameterSet = new HashSet<String>();
		Map<String, Class> regDecs = getRegisteredDecorators();
		// If the given bean is not itself a decorator
		if (beanFactory.findAnnotationOnBean(beanName, Decorator.class) == null) {
			// For all registered decorators find out if the delegate matches the given bean
			for (String decBeanName : regDecs.keySet()) {
				// Scoped targets are ignored since the scoped proxy object should be the decorator instance
				if (decBeanName.startsWith(SCOPED_TARGET))
					continue;
				callbackParameterSet.add(decBeanName);
				// Find the delegate field of the decorator
				ReflectionUtils.doWithFields(regDecs.get(decBeanName), new FieldCallback() {
					@Override
					public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
						if (field.isAnnotationPresent(Delegate.class)) {
							DependencyDescriptor delegateDependencyDescriptor = new DependencyDescriptor(field, true);
							BeanDefinitionHolder thisBeanDefinition = new BeanDefinitionHolder(beanFactory.getBeanDefinition(beanName), beanName);
							// check if the current bean is an autowire candidate for the @Delegate field (also checks for
							// qualifiers)
							if (((DelegateAwareAutowireCandidateResolver) beanFactory.getAutowireCandidateResolver()).isAutowireCandidate2(thisBeanDefinition, delegateDependencyDescriptor)) {
								resolvedDecorators.put((String) callbackParameterSet.toArray()[0], field);
							}
						}
					}
				});
				callbackParameterSet.clear();
			}
		}
		return resolvedDecorators;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, Class> getRegisteredDecorators() {
		Map<String, Class> definitions = new HashMap<String, Class>();
		if (registeredDecoratorsCache == null) {
			registeredDecoratorsCache = new HashMap<String, Class>();
			String[] bdNames = beanFactory.getBeanDefinitionNames();
			for (String bdName : bdNames) {
				BeanDefinition bd = beanFactory.getBeanDefinition(bdName);
				Class targetClass = null;
				if (bd.getOriginatingBeanDefinition() == null) {
					targetClass = getTargetClass(bdName, bd);
				} else {
					targetClass = getTargetClass(bdName, bd.getOriginatingBeanDefinition());
				}
				if (AnnotationUtils.findAnnotation(targetClass, Decorator.class) != null) {
					definitions.put(bdName, targetClass);
					registeredDecoratorsCache.put(bdName, targetClass);
				}
			}
		} else {
			definitions = registeredDecoratorsCache;
		}

		return definitions;
	}

	@SuppressWarnings("rawtypes")
	private Class getTargetClass(String bdName, BeanDefinition bd) {
		Class targetClass = null;
		try {
			targetClass = Class.forName(bd.getBeanClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return targetClass;
	}

}
