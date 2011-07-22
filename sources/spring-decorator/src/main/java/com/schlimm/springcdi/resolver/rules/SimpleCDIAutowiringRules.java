package com.schlimm.springcdi.resolver.rules;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import javax.decorator.Decorator;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import com.schlimm.springcdi.model.DecoratorInfo;
import com.schlimm.springcdi.model.QualifiedDecoratorChain;

public class SimpleCDIAutowiringRules implements DecoratorAutowiringRules {

	private List<QualifiedDecoratorChain> decoratorChains;

	private AutowireCandidateResolver resolver;

	private ConfigurableListableBeanFactory beanFactory;

	public SimpleCDIAutowiringRules() {
		super();
	}

	public SimpleCDIAutowiringRules(List<QualifiedDecoratorChain> decoratorChains, AutowireCandidateResolver resolver, ConfigurableListableBeanFactory beanFactory) {
		super();
		this.decoratorChains = decoratorChains;
		this.resolver = resolver;
		this.beanFactory = beanFactory;
	}

	public boolean applyDecoratorAutowiringRules(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		boolean match = false;
		if (isDecoratedInjectionPoint(descriptor)) {
			// Descriptor refers to a decorated target bean => bdHolder bean name must match last decorator bean name of the
			// decorator chain
			QualifiedDecoratorChain chain = getDecoratorChainForDecoratedInjectionPoint(descriptor);
			DecoratorInfo firstDecoratorInfo = chain.getDecorators().get(0);
			if (firstDecoratorInfo.getDecoratorBeanDefinitionHolder().getBeanName().equals(bdHolder.getBeanName())) {
				return true;
			}
		} else if (isDecorator(descriptor)) {
			QualifiedDecoratorChain chain = getDecoratorChainForDecoratorDescriptor(descriptor);
			// descriptor must be predecessor decorator for bdHolder
			if (chain.areSequential(bdHolder, descriptor))
				return true;
		}
		return match;
	}

	public boolean isDecoratedInjectionPoint(DependencyDescriptor descriptor) {
		// Field is not in a decorator, but descriptor matches a target bean definition
		if (AnnotationUtils.findAnnotation(descriptor.getField().getDeclaringClass(), Decorator.class) == null) {
			// Now that we know, that we're aoutside a @Decorator:
			// is there a chain that contains a target delegate bean definition that matches the descriptor?
			for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
				String delegateName = decoratorChain.getDelegateBeanDefinitionHolder().getBeanName();
				// Check qualifiers and type of the chain's delegate and the descriptor
				if (resolver.isAutowireCandidate(decoratorChain.getDelegateBeanDefinitionHolder(),
						RuleUtils.createRuleBasedDescriptor(descriptor.getField(), new Class[] { DelegateDependencyDescriptorTag.class }))
						&& beanFactory.isTypeMatch(delegateName, descriptor.getDependencyType())) {
					return true;
				}
			}
		}
		return false;
	}

	public QualifiedDecoratorChain getDecoratorChainForDecoratedInjectionPoint(DependencyDescriptor decoratedInjectionPoint) {
		// Match: a chain contains a target bean definition that matches the target descriptor
		for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
			String delegateName = decoratorChain.getDelegateBeanDefinitionHolder().getBeanName();
			// Check qualifiers and type of the chain's delegate and the descriptor
			if (resolver.isAutowireCandidate(decoratorChain.getDelegateBeanDefinitionHolder(),
					RuleUtils.createRuleBasedDescriptor(decoratedInjectionPoint.getField(), new Class[] { DelegateDependencyDescriptorTag.class }))
					&& beanFactory.isTypeMatch(delegateName, decoratedInjectionPoint.getDependencyType())) {
				return decoratorChain;
			}
		}
		return null;
	}

	public boolean isDecorator(DependencyDescriptor dependencyDescriptor) {
		if (AnnotationUtils.findAnnotation(dependencyDescriptor.getField().getDeclaringClass(), Decorator.class) != null) {
			return true;
		}
		return false;
	}

	public QualifiedDecoratorChain getDecoratorChainForDecoratorDescriptor(DependencyDescriptor decoratorDescriptor) {
		for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
			Set<Field> fields = decoratorChain.getAllDeclaredDelegateFields();
			if (fields.contains(decoratorDescriptor.getField())) {
				return decoratorChain;
			}
		}
		return null;
	}

	public List<QualifiedDecoratorChain> getDecoratorChains() {
		return decoratorChains;
	}

	@Override
	public boolean executeLogic(Object... arguments) {
		Assert.isTrue(arguments.length==2, "Expect two arguments!");
		Assert.isTrue(arguments[0] instanceof BeanDefinitionHolder);
		Assert.isTrue(arguments[1] instanceof DependencyDescriptor);
		return applyDecoratorAutowiringRules((BeanDefinitionHolder)arguments[0], (DependencyDescriptor)arguments[1]);
	}

}
