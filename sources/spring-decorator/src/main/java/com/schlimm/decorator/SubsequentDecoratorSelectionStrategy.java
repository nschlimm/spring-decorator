package com.schlimm.decorator;

/**
 * S
 * 
 * @author Niklas Schlimm
 *
 */
public interface SubsequentDecoratorSelectionStrategy {

	String findSuccessorBeanName(String beanName, String[] chainBeanNames);

}
