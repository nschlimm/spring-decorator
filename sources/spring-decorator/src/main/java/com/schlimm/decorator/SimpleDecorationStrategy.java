package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class SimpleDecorationStrategy implements DecorationStrategy {

	@Autowired
	private BeanFactory beanFactory;
	
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
