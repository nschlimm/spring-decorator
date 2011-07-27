package com.schlimm.springcdi.decorator.strategies;

import com.schlimm.springcdi.decorator.model.QualifiedDecoratorChain;

/**
 * Strategy interface for ordering decorators in a {@link QualifiedDecoratorChain}.
 * 
 * @author Niklas Schlimm
 *
 */
public interface DecoratorOrderingStrategy {
	
	/**
	 * Orders the decorators of the given {@link QualifiedDecoratorChain}
	 * @param chain decorators that should be ordered
	 * @return chain with ordered decorators
	 */
	QualifiedDecoratorChain orderDecorators(QualifiedDecoratorChain chain);

}
