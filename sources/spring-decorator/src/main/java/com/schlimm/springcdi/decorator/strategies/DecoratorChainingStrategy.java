package com.schlimm.springcdi.decorator.strategies;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.schlimm.springcdi.model.QualifiedDecoratorChain;

public interface DecoratorChainingStrategy {

	Object getChainedDecorators(ConfigurableListableBeanFactory beanFactory, QualifiedDecoratorChain chain, Object delegate);
	
}
