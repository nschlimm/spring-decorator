package com.schlimm.decorator;

import java.util.Map;

/**
 * {@link DecoratorResolutionStrategy} to implement resolution logic to find decorators for the bean processed in
 * {@link DelegateAwareBeanPostProcessor}.
 * 
 * @author nschlimm
 * 
 */
public interface DecoratorResolutionStrategy {

	@SuppressWarnings("rawtypes")
	Map<String, Class> getRegisteredDecorators();

}
