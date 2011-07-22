package com.schlimm.springcdi;

import com.schlimm.springcdi.resolver.DecoratorAwareAutowireCandidateResolver;
import com.schlimm.springcdi.resolver.rules.SimpleCDIAutowiringRules;

/**
 * Interface implemented by Spring-CDI plugins. Enables registry of rule set plugin with {@link DecoratorAwareAutowireCandidateResolver}.
 * 
 * @author Niklas Schlimm
 * @see {@link SimpleCDIAutowiringRules}, {@link DecoratorAwareAutowireCandidateResolver}
 *
 */
public interface SpringCDIPlugin {
	
	boolean executeLogic(Object... arguments);
	
}
