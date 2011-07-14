package com.schlimm.decorator;

import java.util.ArrayList;
import java.util.List;

import javax.decorator.Decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleDecoratorChain implements DecoratorChain {

	@Autowired
	private DefaultListableBeanFactory beanFactory;

	public String findSuccessorBeanName(String beanName, String[] chainBeanNames) {
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

	@SuppressWarnings("unchecked")
	private List<String> orderChain(String[] candidateNames, DefaultListableBeanFactory beanFactory) {
		List<String> results = new ArrayList<String>();
		String delegateName = null;
		for (String candidateName : candidateNames) {
			@SuppressWarnings("rawtypes")
			Class beanClass = beanFactory.getType(candidateName);
			if (beanClass.isAnnotationPresent(Decorator.class)) {
				results.add(candidateName);
			} else {
				if (delegateName != null) {
					throw new DelegateAwareBeanPostProcessorException("No unique bean candidate resolved for delegate! " + candidateNames);
				} else {
					delegateName = candidateName;
				}
			}
		}
		results.add(delegateName);
		return results;
	}

}
