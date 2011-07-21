package com.schlimm.decorator;

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

import com.schlimm.decorator.resolver.CDIAutowiringRules;
import com.schlimm.decorator.resolver.DecoratorInfo;
import com.schlimm.decorator.resolver.DelegateAwareAutowireCandidateResolver;
import com.schlimm.decorator.resolver.QualifiedDecoratorChain;
import com.schlimm.decorator.resolver.SimpleCDIAutowiringRules;

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

	private CDIAutowiringRules cdiAutowiringRules;

	public DecoratorAwareBeanFactoryPostProcessor() {
		super();
	}

	public DecoratorAwareBeanFactoryPostProcessor(DecoratorResolutionStrategy decoratorResolutionStrategy, DelegateResolutionStrategy delegateResolutionStrategy) {
		super();
		this.decoratorResolutionStrategy = decoratorResolutionStrategy;
		this.delegateResolutionStrategy = delegateResolutionStrategy;
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		DelegateAwareAutowireCandidateResolver newResolver = new DelegateAwareAutowireCandidateResolver();
		newResolver.setBeanFactory(beanFactory);
		((DefaultListableBeanFactory) beanFactory).setAutowireCandidateResolver(newResolver);

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
		if (cdiAutowiringRules == null) {
			cdiAutowiringRules = new SimpleCDIAutowiringRules(chains, newResolver, beanFactory);
		}
		newResolver.setCdiAutowiringRules(cdiAutowiringRules);

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
