package com.schlimm.decorator;

import java.util.List;

/**
 * S
 * 
 * @author Niklas Schlimm
 *
 */
public interface SubsequentDecoratorSelectionStrategy {

	String findSubsequentDecoratorBeanName(String beanName, List<String> chainBeanNames);

}
