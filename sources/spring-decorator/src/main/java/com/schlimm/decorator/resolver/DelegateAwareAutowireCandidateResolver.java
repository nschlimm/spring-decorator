package com.schlimm.decorator.resolver;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.decorator.Delegate;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import com.schlimm.decorator.DelegateDependencyDescriptorTag;

/**
 * {@link AutowireCandidateResolver} that ignores decorator beans for autowiring.
 * 
 * Decorators and delegate implement the same interface. Therefore decorators are autowiring candidates. Without this resolver
 * dependency resolution results in ambiguous bean candidates for decorated bean.
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
		if (descriptor instanceof DelegateDependencyDescriptorTag || (!Arrays.asList(descriptor.getAnnotations()).contains(Delegate.class) && !descriptor.getDependencyType().isInterface()))
			return rawResult;
		return rawResult && applyDecoratorAutowiringRules(bdHolder, descriptor);
	}

	private boolean applyDecoratorAutowiringRules(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		boolean match = false;
		if (isDecoratedInjectionPoint(descriptor)) {
			// Descriptor refers to a decorated target bean => bdHolder bean name must match last decorator bean name
			QualifiedDecoratorChain chain = getDecoratorChainForDecoratedInjectionPoint(descriptor);
			DecoratorInfo lastDecoratorInfo = chain.getDecorators().get(chain.getDecorators().size()-1);
			if (lastDecoratorInfo.getDecoratorBeanDefinitionHolder().getBeanName().equals(bdHolder.getBeanName())) {
				return true;
			}
		} else if (isDecorator(descriptor)) {
			QualifiedDecoratorChain chain = getDecoratorChainForDecoratorDescriptor(descriptor);
			// descriptor must be predecessor decorator for bdHolder
			if(chain.areSequential(bdHolder, descriptor))
				return true;
		}
		return match;
	}

	public boolean isAutowireCandidate2(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		return super.isAutowireCandidate(bdHolder, descriptor);
	}

	public boolean isDecoratedInjectionPoint(DependencyDescriptor descriptor) {
		// If the injection point contains @Delegate it cannot be a decorated injection point
		if (Arrays.asList(descriptor.getAnnotations()).contains(Delegate.class)) {
			return false;
		}
		// Now that we know, that we're aoutside a @Decorator:
		// The injection point is decorated if one decorator chain contains the matching target delegate bean definition
		for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
			if (super.isAutowireCandidate(decoratorChain.getDelegateBeanDefinitionHolder(), descriptor)){
				return true;
			}
		}
		return false;
	}

	public QualifiedDecoratorChain getDecoratorChainForDecoratedInjectionPoint(DependencyDescriptor decoratedInjectionPoint) {
		// Match: a chain contains a target bean definition that matches the target descriptor
		// There can be multiple decorator chains for the same target
		// Therefore: injection point qualifiers must match chain qualifiers (includes type comparison)
		for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
			if (super.isAutowireCandidate(decoratorChain.getDelegateBeanDefinitionHolder(), decoratedInjectionPoint)){
				QualifiedDependencyDescription ipQDesc = new QualifiedDependencyDescription(decoratedInjectionPoint);
				if (decoratorChain.getQualifiedDelegateDependencyDescription().equals(ipQDesc)) {
					return decoratorChain;
				}
			}
		}
		return null;
	}

	public boolean isDecorator(DependencyDescriptor dependencyDescriptor) {
		if (Arrays.asList(dependencyDescriptor.getAnnotations()).contains(Delegate.class)) {
			return true;
		}
		return false;
	}
	public QualifiedDecoratorChain getDecoratorChainForDecoratorDescriptor(DependencyDescriptor decoratorDescriptor) {
		QualifiedDependencyDescription qDesc = new QualifiedDependencyDescription(decoratorDescriptor);
		for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
			if (decoratorChain.getQualifiedDelegateDependencyDescription().equals(qDesc)){
				return decoratorChain;
			}
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
