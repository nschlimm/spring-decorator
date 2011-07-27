package com.schlimm.springcdi.resolver.rules;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

import com.schlimm.springcdi.SpringCDIPlugin;

/**
 * Interface that is implemented by decorator autowiring rule sets.
 * 
 * Clients can implement their own rule sets to enhance wiring logic.
 * 
 * @author Niklas Schlimm
 *
 */
public interface DecoratorAutowiringRules extends SpringCDIPlugin {

	boolean applyDecoratorAutowiringRules(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor);

}
