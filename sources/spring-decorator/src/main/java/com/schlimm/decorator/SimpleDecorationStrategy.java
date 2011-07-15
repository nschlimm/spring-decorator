package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.ReflectionUtils;

/**
 * Simple implementation of {@link DecorationStrategy} that chains decorators as they are listet in the bean factory.
 * 
 * @author Niklas Schlimm
 *
 */
public class SimpleDecorationStrategy implements DecorationStrategy {

	private BeanFactory beanFactory;
	
	public SimpleDecorationStrategy(BeanFactory beanFactory) {
		super();
		this.beanFactory = beanFactory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object decorateDelegate(Object delegate, SortedMap<String, Field> resolvedDecorators) {
		Object primaryDecorator = null;
		for (int i = 0; i < resolvedDecorators.keySet().size(); i++) {
			Entry<String, Field> thisEntry = (Map.Entry<String, Field>)resolvedDecorators.entrySet().toArray()[i];
			Object thisDecorator = beanFactory.getBean(thisEntry.getKey());
			if (primaryDecorator == null) {
				primaryDecorator = thisDecorator;
			}
			// If this decorator is a proxy object then we need to retrieve the target to do the actual injection stuff
			// CGLIB Proxies are interceptors, thus different objects then their targets
			if (AopUtils.isCglibProxy(thisDecorator)){
				thisDecorator = beanFactory.getBean("scopedTarget."+thisEntry.getKey());
			}
			if (resolvedDecorators.keySet().size() == i + 1) {
				inject(thisEntry.getValue(), thisDecorator, delegate);
				break;
			}
			Entry<String, Field> nextEntry = (Map.Entry<String, Field>)resolvedDecorators.entrySet().toArray()[i+1];
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
