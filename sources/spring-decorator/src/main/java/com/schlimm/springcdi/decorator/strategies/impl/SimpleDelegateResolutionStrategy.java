package com.schlimm.springcdi.decorator.strategies.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ClassUtils;

import com.schlimm.springcdi.decorator.DecoratorAwareBeanFactoryPostProcessorException;
import com.schlimm.springcdi.decorator.DecoratorModuleUtils;
import com.schlimm.springcdi.decorator.model.DecoratorInfo;
import com.schlimm.springcdi.decorator.model.DelegateField;
import com.schlimm.springcdi.decorator.resolver.rules.IgnoreDecoratorAutowiringLogic;
import com.schlimm.springcdi.decorator.strategies.DelegateResolutionStrategy;

/**
 * Simple strategy that searches the delegate for the given decorator.
 * 
 * @author Niklas Schlimm
 *
 */
public class SimpleDelegateResolutionStrategy implements DelegateResolutionStrategy {

	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public String getRegisteredDelegate(ConfigurableListableBeanFactory beanFactory, DecoratorInfo decoratorInfo) {
		DelegateField arbitraryDelegateField = decoratorInfo.getDelegateFields().get(0);
		DependencyDescriptor delegateDependencyDescriptor = DecoratorModuleUtils.createRuleBasedDescriptor(arbitraryDelegateField.getDeclaredField(), new Class[] { IgnoreDecoratorAutowiringLogic.class });
		List<String> registeredDelegates = new ArrayList<String>();
		String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, arbitraryDelegateField.getDeclaredField().getType(), true, false);
		for (String candidate : candidateNames) {
			BeanDefinition bd = beanFactory.getBeanDefinition(candidate);
			// Annotated Bean scanned in the classpath
			if (bd instanceof AnnotatedBeanDefinition) {
				AnnotatedBeanDefinition abd = (AnnotatedBeanDefinition) bd;
				// No @Decorator
				if (!DecoratorInfo.isDecorator(abd)) {
					Class decoratorClass = null;
					try {
						decoratorClass = ClassUtils.forName(abd.getBeanClassName(), this.getClass().getClassLoader());
					} catch (Exception e) {
						throw new DecoratorAwareBeanFactoryPostProcessorException("Could not find decorator class: " + abd.getBeanClassName(), e);
					}
					// Consider scoped proxies
					if (candidate.startsWith("scopedTarget.")) {
						candidate = candidate.replace("scopedTarget.", "");
					}
					// Bean must match delegate dependency descriptor (that describes the delegate field of the given decorator)
					if ((((DefaultListableBeanFactory) beanFactory).isAutowireCandidate(candidate, delegateDependencyDescriptor))) {
						registeredDelegates.add(candidate);
					}
				}
			}
		}
		if (registeredDelegates.size() > 1) {
			throw new DecoratorAwareBeanFactoryPostProcessorException("Could not find unique delegate for decorator info: " + decoratorInfo.toString());
		}
		return registeredDelegates.get(0);
	}

}
