package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.ReflectionUtils;

/**
 * Simple implementation of {@link DelegateDecorationStrategy} that chains decorators as they are listet in the bean factory.
 * Supports scoped beans and CGLIB proxies.
 * 
 * @author Niklas Schlimm
 * 
 */
public class SimpleDecorationStrategy implements DelegateDecorationStrategy {

	private static final String SCOPED_TARGET = "scopedTarget.";
	
	private BeanFactory beanFactory;

	public SimpleDecorationStrategy(BeanFactory beanFactory) {
		super();
		this.beanFactory = beanFactory;
	}

	/**
	 * This method implements the decoration strategy for {@link SimpleDecorationStrategy}.
	 * 
	 * If a decorator is a proxy object then we need to retrieve the target to do the actual injection stuff
	 * CGLIB Proxies are interceptors, thus different objects then their targets, the successing decorator however needs
	 * to be injected into the target bean.
     *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object decorateDelegate(Object delegate, SortedMap<String, Field> resolvedDecorators) {
		if (delegate.getClass().equals(ScopedProxyFactoryBean.class)) delegate = ((ScopedProxyFactoryBean)delegate).getObject();
		Object primaryDecorator = null;
		for (int i = 0; i < resolvedDecorators.keySet().size(); i++) {
			Entry<String, Field> thisEntry = (Map.Entry<String, Field>) resolvedDecorators.entrySet().toArray()[i];
			Object thisDecorator = beanFactory.getBean(thisEntry.getKey());
			if (primaryDecorator == null) {
				primaryDecorator = thisDecorator;
			}
			if (AopUtils.isAopProxy(thisDecorator)&&beanFactory.containsBean(SCOPED_TARGET + thisEntry.getKey())) {
				thisDecorator = beanFactory.getBean(SCOPED_TARGET + thisEntry.getKey());
			}
			if (resolvedDecorators.keySet().size() == i + 1) {
				inject(thisEntry.getValue(), thisDecorator, delegate);
				break;
			}
			Entry<String, Field> nextEntry = (Map.Entry<String, Field>) resolvedDecorators.entrySet().toArray()[i + 1];
			Object subsequentDecorator = beanFactory.getBean(nextEntry.getKey());
			inject(thisEntry.getValue(), thisDecorator, subsequentDecorator);
		}
		return primaryDecorator;
	}

	private void inject(Field field, Object parentBean, Object beanToInject) {
		ReflectionUtils.makeAccessible(field);
		try {
			field.set(parentBean, beanToInject);
		} catch (Exception e) {
			throw new DelegateAwareBeanPostProcessorException("Could not inject delegate!", e);
		}
	}

}
