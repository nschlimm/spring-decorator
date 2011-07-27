package com.schlimm.springcdi;

import com.schlimm.springcdi.decorator.resolver.DecoratorAwareAutowireCandidateResolver;
import com.schlimm.springcdi.decorator.resolver.rules.BeanPostProcessorCDIAutowiringRules;
import com.schlimm.springcdi.decorator.resolver.rules.ResolverCDIAutowiringRules;

/**
 * Interface implemented by Spring-CDI plugins. Enables registry of rule set plugin with {@link DecoratorAwareAutowireCandidateResolver}.
 * 
 * @author Niklas Schlimm
 * @see {@link ResolverCDIAutowiringRules}, {@link DecoratorAwareAutowireCandidateResolver}, {@link BeanPostProcessorCDIAutowiringRules}
 *
 */
public interface SpringCDIPlugin {
	
	boolean executeLogic(Object... arguments);
	
}
