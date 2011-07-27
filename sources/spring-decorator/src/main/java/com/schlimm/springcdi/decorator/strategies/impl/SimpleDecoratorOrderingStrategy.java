package com.schlimm.springcdi.decorator.strategies.impl;

import com.schlimm.springcdi.decorator.strategies.DecoratorOrderingStrategy;
import com.schlimm.springcdi.decorator.strategies.DecoratorResolutionStrategy;
import com.schlimm.springcdi.model.QualifiedDecoratorChain;

/**
 * Simple ordering strategy just orders decorators as found in the {@link DecoratorResolutionStrategy}.
 * 
 * @author Niklas Schlimm
 *
 */
public class SimpleDecoratorOrderingStrategy implements DecoratorOrderingStrategy {

	@Override
	public QualifiedDecoratorChain orderDecorators(QualifiedDecoratorChain chain) {
		return chain;
	}

}
