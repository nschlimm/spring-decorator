package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.SortedMap;

/**
 * {@link DecoratorResolutionStrategy} to implement resolution logic to find decorators for the bean processed in
 * {@link DelegateAwareBeanPostProcessor}.
 * 
 * @author nschlimm
 * 
 */
public interface DecoratorResolutionStrategy {

	SortedMap<String, Field> resolveDecorators(final Object bean, final String beanName);

}
