package com.schlimm.decorator;

import java.util.List;

import javax.decorator.Decorator;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;


@Component
public class SimplePrimaryDecoratorSelectionStrategy implements PrimaryBeanSelectionStrategy {

	@SuppressWarnings({ "rawtypes" })
	@Override
	public String determineUniquePrimaryCandidate(List<String> candidateBeanNames, BeanFactory beanFactory) {
		if (candidateBeanNames.size()==1) return candidateBeanNames.get(0);
		for (String autowireCandidate : candidateBeanNames) {
			Class matchingBeanClass = beanFactory.getType(autowireCandidate);
			// Simple strategy selects first decorator for this delegate type to be the primary decorator
			if (AnnotationUtils.findAnnotation(matchingBeanClass, Decorator.class)!=null) {
				return autowireCandidate;
			}
		}
		return null;
	}
}
