package com.schlimm.decorator;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.decorator.Decorator;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * {@link AutowireCandidateResolver} that ignores decorator beans for autowiring.
 * 
 * @author Niklas Schlimm
 *
 */
public class DelegateAwareAutowireCandidateResolver extends QualifierAnnotationAutowireCandidateResolver {

	private BeanFactory beanFactory;
	
	public DelegateAwareAutowireCandidateResolver() {
		super();
	}

	public DelegateAwareAutowireCandidateResolver(Class<? extends Annotation> qualifierType) {
		super(qualifierType);
	}

	public DelegateAwareAutowireCandidateResolver(Set<Class<? extends Annotation>> qualifierTypes) {
		super(qualifierTypes);
	}

	public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		return super.isAutowireCandidate(bdHolder, descriptor) && AnnotationUtils.findAnnotation(beanFactory.getType(bdHolder.getBeanName()),Decorator.class)==null;
	}

	public boolean isAutowireCandidate2(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		return super.isAutowireCandidate(bdHolder, descriptor);
	}
	
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
}
