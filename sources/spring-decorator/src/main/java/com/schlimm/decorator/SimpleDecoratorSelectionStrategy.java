package com.schlimm.decorator;

import java.util.ArrayList;
import java.util.List;

import javax.decorator.Decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;


@Component
public class SimpleDecoratorSelectionStrategy implements SubsequentDecoratorSelectionStrategy {

	@Autowired
	private DefaultListableBeanFactory beanFactory;

	public String findSubsequentDecoratorBeanName(String beanName, List<String> chainBeanNames) {
		List<String> orderedDecoratorNames = orderChain(chainBeanNames, (DefaultListableBeanFactory) beanFactory);
		String nextBeanName = null;
		boolean takeNext = false;
		if (orderedDecoratorNames.size() == 1) {
			takeNext = true;
		}
		for (String name : orderedDecoratorNames) {
			if (takeNext) {
				nextBeanName = name;
				break;
			}
			if (name.equals(beanName)) {
				takeNext = true;
			}
		}
		if (nextBeanName == null) {
			throw new DelegateAwareBeanPostProcessorException("Could not find successor for bean name: " + beanName);
		}
		return nextBeanName;
	}

	private List<String> orderChain(List<String> chainBeanNames, DefaultListableBeanFactory beanFactory) {
		List<String> results = new ArrayList<String>();
		String delegateName = null;
		for (String candidateName : chainBeanNames) {
			@SuppressWarnings("rawtypes")
			Class beanClass = beanFactory.getType(candidateName);
			if (AnnotationUtils.findAnnotation(beanClass, Decorator.class)!=null) {
				results.add(candidateName);
			} else {
				if (delegateName != null) {
					throw new DelegateAwareBeanPostProcessorException("No unique bean candidate resolved for delegate! " + chainBeanNames);
				} else {
					delegateName = candidateName;
				}
			}
		}
		results.add(delegateName);
		return results;
	}

}
