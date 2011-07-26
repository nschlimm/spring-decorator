package com.schlimm.springcdi.resolver;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import com.schlimm.springcdi.SpringCDIInfrastructure;
import com.schlimm.springcdi.SpringCDIPlugin;

/**
 * {@link AutowireCandidateResolver} that can wire CDI decorators. 
 * 
 * Clients may register custom rule sets if the wiring logic is not sufficient enough.
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
		// Check registered wiring rules
		boolean ruleSetResultOK = true;
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

	@Override
	public Set<SpringCDIPlugin> getPlugins() {
		return rulePlugins;
	}
}
