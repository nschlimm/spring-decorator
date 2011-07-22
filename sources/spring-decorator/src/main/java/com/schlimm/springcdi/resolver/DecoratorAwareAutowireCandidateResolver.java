package com.schlimm.springcdi.resolver;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.decorator.Delegate;

import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import com.schlimm.springcdi.SpringCDIInfrastructure;
import com.schlimm.springcdi.SpringCDIPlugin;
import com.schlimm.springcdi.resolver.rules.DelegateDependencyDescriptorTag;

/**
 * {@link AutowireCandidateResolver} that ignores decorator beans for autowiring.
 * 
 * Decorators and delegate implement the same interface. Therefore decorators are autowiring candidates. Without this resolver
 * dependency resolution results in ambiguous bean candidates for decorated bean.
 * 
 * @author Niklas Schlimm
 * 
 */
public class DecoratorAwareAutowireCandidateResolver extends QualifierAnnotationAutowireCandidateResolver implements SpringCDIInfrastructure {

	private Set<SpringCDIPlugin> rulePlugins = new HashSet<SpringCDIPlugin>();

	public DecoratorAwareAutowireCandidateResolver() {
		super();
	}

	public DecoratorAwareAutowireCandidateResolver(Class<? extends Annotation> qualifierType) {
		super(qualifierType);
	}

	public DecoratorAwareAutowireCandidateResolver(Set<Class<? extends Annotation>> qualifierTypes) {
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
		boolean ruleSetResultOK = false;
		for (SpringCDIPlugin rulePlugin : rulePlugins) {
			boolean rulesResultOK = rulePlugin.executeLogic(bdHolder, descriptor);
			if (!rulesResultOK) {
				ruleSetResultOK = false;
			}
		}
		return rawResult && ruleSetResultOK;
	}

	@Override
	public void addPlugin(SpringCDIPlugin plugin) {
		rulePlugins.add(plugin);
	}
}
