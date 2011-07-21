package com.schlimm.decorator.resolver;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.decorator.Delegate;

import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import com.schlimm.decorator.resolver.descriptorrules.DelegateDependencyDescriptorTag;

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

	private CDIAutowiringRules cdiAutowiringRules;

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
		// First check qualifiers
		boolean rawResult = super.isAutowireCandidate(bdHolder, descriptor);
		if (rawResult==false) return rawResult;
		boolean isDelegateDescriptor = false;
		if (descriptor.getField().getAnnotation(Delegate.class)!=null) isDelegateDescriptor = true;
		if (descriptor instanceof DelegateDependencyDescriptorTag || (!isDelegateDescriptor && !descriptor.getDependencyType().isInterface()))
			return rawResult;
		boolean decoratorRulesResult = cdiAutowiringRules.applyDecoratorAutowiringRules(bdHolder, descriptor);
		return rawResult && decoratorRulesResult;
	}

	public CDIAutowiringRules getCdiAutowiringRules() {
		return cdiAutowiringRules;
	}

	public void setCdiAutowiringRules(CDIAutowiringRules cdiAutowiringRules) {
		this.cdiAutowiringRules = cdiAutowiringRules;
	}
}
