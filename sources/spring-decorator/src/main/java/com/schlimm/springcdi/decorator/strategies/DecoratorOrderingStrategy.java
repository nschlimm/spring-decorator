package com.schlimm.springcdi.decorator.strategies;

import com.schlimm.springcdi.model.QualifiedDecoratorChain;

/**
 * Strategy interface for ordering decorators in a {@link QualifiedDecoratorChain}.
 * 
 * @author Niklas Schlimm
 *
 */
public interface DecoratorOrderingStrategy {
	
	QualifiedDecoratorChain orderDecorators(QualifiedDecoratorChain chain);

}
