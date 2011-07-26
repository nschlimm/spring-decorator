package com.schlimm.springcdi.resolver.rules;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import com.schlimm.springcdi.model.DecoratorInfo;
import com.schlimm.springcdi.model.QualifiedDecoratorChain;

/**
 * Class implements the wiring rules for autowiring CDI decorators.
 * 
 * @author Niklas Schlimm
 * 
 */
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

	/**
	 * Main method that is called to check if the given {@link BeanDefinitionHolder} is a candidate for the given injection point
	 * described by the {@link DependencyDescriptor}.
	 * 
	 * @param bdHolder
	 *            candidate bean
	 * @param descriptor
	 *            injection point
	 * @return true if candidate bean can be wired into injection point
	 * 
	 */
	public boolean applyDecoratorAutowiringRules(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		boolean isDelegateDescriptor = false;
		if (descriptor.getField().getAnnotation(Delegate.class) != null)
			isDelegateDescriptor = true;
		if (descriptor instanceof IgnoreDecoratorAutowiringLogic || (!isDelegateDescriptor && !descriptor.getDependencyType().isInterface()))
			return true;
		boolean match = false;
		if (isDecoratedInjectionPoint(descriptor)) {
			// Descriptor refers to a decorated target bean => bdHolder bean name must match last decorator bean name of the
			// decorator chain
			QualifiedDecoratorChain chain = getDecoratorChainForDecoratedInjectionPoint(descriptor);
			DecoratorInfo firstDecoratorInfo = chain.getDecorators().get(0);
			if (firstDecoratorInfo.getDecoratorBeanDefinitionHolder().getBeanName().equals(bdHolder.getBeanName())) {
				return true;
			}
		} else if (DecoratorInfo.isDecorator(descriptor)) {
			QualifiedDecoratorChain chain = getDecoratorChainForDecoratorDescriptor(descriptor);
			// descriptor must be predecessor decorator for bdHolder
			if (chain.areSequential(bdHolder, descriptor))
				return true;
		}
		return match;
	}

	/**
	 * Method checks if the given descriptor is a decorated injection point. That is, the injection point matches a delegate bean
	 * definition of a known {@link QualifiedDecoratorChain}.
	 * 
	 * @param descriptor injection point to check
	 * @return true if injection point must be decorated
	 */
	public boolean isDecoratedInjectionPoint(DependencyDescriptor descriptor) {
		// Field is not in a decorator, but descriptor matches a target bean definition
		if (AnnotationUtils.findAnnotation(descriptor.getField().getDeclaringClass(), Decorator.class) == null) {
			// Now that we know, that we're aoutside a @Decorator:
			// is there a chain that contains a target delegate bean definition that matches the descriptor?
			for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
				String delegateName = decoratorChain.getDelegateBeanDefinitionHolder().getBeanName();
				// Check qualifiers and type of the chain's delegate and the descriptor
				if (resolver.isAutowireCandidate(decoratorChain.getDelegateBeanDefinitionHolder(),
						RuleUtils.createRuleBasedDescriptor(descriptor.getField(), new Class[] { IgnoreDecoratorAutowiringLogic.class }))
						&& beanFactory.isTypeMatch(delegateName, descriptor.getDependencyType())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get the {@link QualifiedDecoratorChain} for a decorated injection point
	 * 
	 * @param decoratedInjectionPoint
	 * @return {@link QualifiedDecoratorChain} that applies to the injection point
	 */
	public QualifiedDecoratorChain getDecoratorChainForDecoratedInjectionPoint(DependencyDescriptor decoratedInjectionPoint) {
		// Match: a chain contains a target bean definition that matches the target descriptor
		for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
			String delegateName = decoratorChain.getDelegateBeanDefinitionHolder().getBeanName();
			// Check qualifiers and type of the chain's delegate and the descriptor
			if (resolver.isAutowireCandidate(decoratorChain.getDelegateBeanDefinitionHolder(),
					RuleUtils.createRuleBasedDescriptor(decoratedInjectionPoint.getField(), new Class[] { IgnoreDecoratorAutowiringLogic.class }))
					&& beanFactory.isTypeMatch(delegateName, decoratedInjectionPoint.getDependencyType())) {
				return decoratorChain;
			}
		}
		return null;
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
		Assert.isTrue(arguments.length == 2, "Expect two arguments!");
		Assert.isTrue(arguments[0] instanceof BeanDefinitionHolder);
		Assert.isTrue(arguments[1] instanceof DependencyDescriptor);
		return applyDecoratorAutowiringRules((BeanDefinitionHolder) arguments[0], (DependencyDescriptor) arguments[1]);
	}

}
