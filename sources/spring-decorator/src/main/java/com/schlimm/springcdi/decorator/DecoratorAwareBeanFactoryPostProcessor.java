package com.schlimm.springcdi.decorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;

import com.schlimm.springcdi.SpringCDIInfrastructure;
import com.schlimm.springcdi.decorator.strategies.DecoratorResolutionStrategy;
import com.schlimm.springcdi.decorator.strategies.DelegateResolutionStrategy;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDecoratorResolutionStrategy;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDelegateResolutionStrategy;
import com.schlimm.springcdi.model.DecoratorInfo;
import com.schlimm.springcdi.model.QualifiedDecoratorChain;
import com.schlimm.springcdi.resolver.DecoratorAwareAutowireCandidateResolver;
import com.schlimm.springcdi.resolver.rules.DecoratorAutowiringRules;
import com.schlimm.springcdi.resolver.rules.SimpleCDIAutowiringRules;

/**
 * This {@link BeanFactoryPostProcessor} sets custom {@link AutowireCandidateResolver} that ignores decorators for autowiring
 * 
 * @author Niklas Schlimm
 * 
 */
@SuppressWarnings("rawtypes")
public class DecoratorAwareBeanFactoryPostProcessor implements BeanFactoryPostProcessor, Ordered, InitializingBean {

	protected final Log logger = LogFactory.getLog(getClass());

	private DecoratorResolutionStrategy decoratorResolutionStrategy;

	private DelegateResolutionStrategy delegateResolutionStrategy;

	private DecoratorAutowiringRules decoratorAutowiringRules;

	public DecoratorAwareBeanFactoryPostProcessor() {
		super();
	}

	public DecoratorAwareBeanFactoryPostProcessor(DecoratorResolutionStrategy decoratorResolutionStrategy, DelegateResolutionStrategy delegateResolutionStrategy) {
		super();
		this.decoratorResolutionStrategy = decoratorResolutionStrategy;
		this.delegateResolutionStrategy = delegateResolutionStrategy;
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		Map<String, Class> decorators = decoratorResolutionStrategy.getRegisteredDecorators(beanFactory);
		List<DecoratorInfo> decoratorInfos = new ArrayList<DecoratorInfo>();
		List<QualifiedDecoratorChain> chains = new ArrayList<QualifiedDecoratorChain>();
		for (String bdName : decorators.keySet()) {
			DecoratorInfo newDecoratorInfo = new DecoratorInfo(bdName, beanFactory.getBeanDefinition(bdName), decorators.get(bdName));
			decoratorInfos.add(newDecoratorInfo);
			String delegate = delegateResolutionStrategy.getRegisteredDelegate(beanFactory, newDecoratorInfo);
			QualifiedDecoratorChain chain = null;
			// Gibt es schon eine chain für den delegate?
			for (QualifiedDecoratorChain qualifiedDecoratorChain : chains) {
				if (qualifiedDecoratorChain.getDelegateBeanDefinitionHolder().getBeanName().equals(delegate)) {
					chain = qualifiedDecoratorChain;
				}
			}
			if (chain == null) {
				chain = new QualifiedDecoratorChain(new BeanDefinitionHolder(beanFactory.getBeanDefinition(delegate), delegate));
				chains.add(chain);
			}
			chain.addDecoratorInfo(newDecoratorInfo);
		}
		
		AutowireCandidateResolver resolver = ((DefaultListableBeanFactory) beanFactory).getAutowireCandidateResolver();
		if (resolver instanceof SpringCDIInfrastructure) {
			((SpringCDIInfrastructure)resolver).addPlugin(decoratorAutowiringRules == null ? new SimpleCDIAutowiringRules(chains, resolver, beanFactory) : decoratorAutowiringRules);
		} else {
			DecoratorAwareAutowireCandidateResolver newResolver = new DecoratorAwareAutowireCandidateResolver();
			newResolver.setBeanFactory(beanFactory);
			((DefaultListableBeanFactory) beanFactory).setAutowireCandidateResolver(newResolver);
			
			if (decoratorAutowiringRules == null) {
				decoratorAutowiringRules = new SimpleCDIAutowiringRules(chains, newResolver, beanFactory);
			}
			newResolver.addPlugin(decoratorAutowiringRules);
		}

	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (decoratorResolutionStrategy == null) {
			decoratorResolutionStrategy = new SimpleDecoratorResolutionStrategy();
		}
		if (delegateResolutionStrategy == null) {
			delegateResolutionStrategy = new SimpleDelegateResolutionStrategy();
		}
	}

}
