package com.schlimm.springcdi.decorator.strategies;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.schlimm.springcdi.decorator.model.QualifiedDecoratorChain;

/**
 * Strategy that chains decorators and the delegate. Implementations perform the actual injection (1) of the chained decorator and
 * (2) the delegate into the last decorator in the chain.
 * 
 * @author Niklas Schlimm
 * 
 */
public interface DecoratorChainingStrategy {

	/**
	 * Injects the decorators into each other in the order given in the {@link QualifiedDecoratorChain}.
	 * 
	 * @param beanFactory current bean factory that knows the decorators
	 * @param chain {@link QualifiedDecoratorChain} that carries the decorator meta data
	 * @param delegate the decorated target bean
	 * @return the decorator chain
	 */
	Object getChainedDecorators(ConfigurableListableBeanFactory beanFactory, QualifiedDecoratorChain chain, Object delegate);

}
