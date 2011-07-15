package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

/**
 * {@link BeanPostProcessor} that scanns beans to find decorators for the current bean. It will then chain the decorators and return the 
 * primary decorator that gets injected for the decorated delegate bean type.
 * 
 * Allows custom implementation of decoration strategy.
 * 
 * @author Niklas Schlimm
 * 
 */
public class AlternateDelegateAwareBeanPostProcessor implements BeanPostProcessor, InitializingBean, BeanFactoryAware {

	private DefaultListableBeanFactory beanFactory;

	private DecorationStrategy decorationStrategy;

	public AlternateDelegateAwareBeanPostProcessor() {
		super();
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
		String[] bdNames = beanFactory.getBeanDefinitionNames();
		final SortedMap<String, Field> resolvedDecorators = new TreeMap<String, Field>();
		final Set<String> callbackParameterSet = new HashSet<String>();
		// Nur wenn die aktuelle bean kein decorator ist
		if (AnnotationUtils.findAnnotation(bean.getClass(), Decorator.class) == null) {
			for (String bdName : bdNames) {
				// Scoped targets are ignored since the proxy is the decorator instance
				if (bdName.startsWith("scopedTarget.")) continue;
				callbackParameterSet.add(bdName);
				// Decorator nach delegate feld durchsuchen, nur dann ist der decorator interessant
				if (beanFactory.findAnnotationOnBean(bdName, Decorator.class)!=null) {
					// store all @Delegate fields that match the delegate bean type (that is, the actual bean type)
					ReflectionUtils.doWithFields(beanFactory.getType(bdName), new FieldCallback() {
						@Override
						public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
							if (field.isAnnotationPresent(Delegate.class)) {
								// die aktuelle bean muss auf das delegate feld zuweisbar sein (notwendige Bedingung) - wir suchen ja delegate felder für diese bean
								if (field.getType().isAssignableFrom(bean.getClass())) {
									DependencyDescriptor delegateDependencyDescriptor = new DependencyDescriptor(field, true);
									BeanDefinitionHolder thisBeanDefinition = new BeanDefinitionHolder(beanFactory.getBeanDefinition(beanName), beanName);
									if (((DelegateAwareAutowireCandidateResolver) beanFactory.getAutowireCandidateResolver()).isAutowireCandidate2(thisBeanDefinition, delegateDependencyDescriptor)) {
										resolvedDecorators.put((String)callbackParameterSet.toArray()[0], field);
									}
								}
							}
						}
					});
				}
				callbackParameterSet.clear();
			}
		}
		if (resolvedDecorators.size() > 0) {
			return decorationStrategy.decorateDelegate(bean, resolvedDecorators);
		} else {
			return bean;
		}
	}

	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	public void setDecorationStrategy(DecorationStrategy decorationStrategy) {
		this.decorationStrategy = decorationStrategy;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		if (decorationStrategy==null) {
			decorationStrategy = new SimpleDecorationStrategy(beanFactory);
		}
		
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory)beanFactory;
	}

}
