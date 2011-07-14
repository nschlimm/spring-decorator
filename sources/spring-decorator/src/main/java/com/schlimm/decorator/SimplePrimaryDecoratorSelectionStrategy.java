package com.schlimm.decorator;

import javax.decorator.Decorator;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;


@Component
public class SimplePrimaryDecoratorSelectionStrategy implements PrimaryBeanSelectionStrategy {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String determineUniquePrimaryCandidate(String[] candidateBeanNames, BeanFactory beanFactory) {
		if (candidateBeanNames.length==1) return candidateBeanNames[0];
		for (String matchingBeanName : candidateBeanNames) {
			Class matchingBeanClass = beanFactory.getType(matchingBeanName);
			// Simple strategy selects first decorator for this delegate type to be the primary decorator
			if (matchingBeanClass.isAnnotationPresent(Decorator.class)) {
				return matchingBeanName;
			}
		}
		return null;
	}
}
