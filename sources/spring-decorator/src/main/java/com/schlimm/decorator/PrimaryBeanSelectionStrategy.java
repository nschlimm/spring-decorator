package com.schlimm.decorator;

import java.util.List;

import org.springframework.beans.factory.BeanFactory;

/**
 * Strategy interface to be implemented to determine the primary bean if more then one decorator decorates the same delegate bean.
 * 
 * @author Niklas Schlimm
 *
 */
public interface PrimaryBeanSelectionStrategy {
	
	/**
	 * Select the primary candidate out of the list of passed candidate decorator bean names. 
	 * @param candidateBeanNames the bean candidates to select from
	 * @param beanFactory the bean factory that contains the bean definitions
	 * @return the decorator bean name to be injected into client references to the delegate bean
	 */
	String determineUniquePrimaryCandidate(List<String> candidateBeanNames, BeanFactory beanFactory);

}
