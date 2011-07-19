package com.schlimm.decorator.resolver;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import com.schlimm.decorator.DelegateDependencyDescriptorTag;

/**
 * {@link AutowireCandidateResolver} that ignores decorator beans for autowiring. 
 * 
 * Decorators and delegate implement the same interface. Therefore decorators are autowiring candidates. 
 * Without this resolver dependency resolution results in ambiguous bean candidates for decorated bean.
 * 
 * @author Niklas Schlimm
 *
 */
public class DelegateAwareAutowireCandidateResolver extends QualifierAnnotationAutowireCandidateResolver {

	private BeanFactory beanFactory;
	
	private List<QualifiedDecoratorChain> decoratorChains;
	
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
		boolean rawResult = super.isAutowireCandidate(bdHolder, descriptor);
		if (descriptor instanceof DelegateDependencyDescriptorTag) return rawResult;
		boolean chainOK = false;
		if (isDecorated(descriptor)) {
			QualifiedDecoratorChain chain = getDecoratorChain(descriptor);
			chainOK = chain.validateAutowireCandidate(bdHolder, descriptor);
		}
		return  rawResult && chainOK;
	}

	public boolean isAutowireCandidate2(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		return super.isAutowireCandidate(bdHolder, descriptor);
	}
	
	public boolean isDecorated(DependencyDescriptor descriptor) {
		QualifiedDependencyDescription qDesc = new QualifiedDependencyDescription(descriptor);
		for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
			if (decoratorChain.getQualifiedDependencyDescription().equals(qDesc)) return true;
		}
		return false;
	}
	
	public QualifiedDecoratorChain getDecoratorChain(DependencyDescriptor descriptor) {
		QualifiedDependencyDescription qDesc = new QualifiedDependencyDescription(descriptor);
		for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
			if (decoratorChain.getQualifiedDependencyDescription().equals(qDesc)) return decoratorChain;
		}
		return null;
	}
	
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public List<QualifiedDecoratorChain> getDecoratorChains() {
		return decoratorChains;
	}

	public void setDecoratorChains(List<QualifiedDecoratorChain> decoratorChains) {
		this.decoratorChains = decoratorChains;
	}
}
