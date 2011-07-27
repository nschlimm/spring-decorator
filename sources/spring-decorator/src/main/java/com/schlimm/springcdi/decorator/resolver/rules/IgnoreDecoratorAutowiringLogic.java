package com.schlimm.springcdi.decorator.resolver.rules;

import org.springframework.beans.factory.config.DependencyDescriptor;

import com.schlimm.springcdi.decorator.resolver.DecoratorAwareAutowireCandidateResolver;

/**
 * Tagging interface to mark a {@link DependencyDescriptor} that was instantiated during decorator autowiring logic. The
 * {@link DecoratorAwareAutowireCandidateResolver} will ignore specific {@link DecoratorAutowiringRules} for
 * {@link DependencyDescriptor} that is marked with this interface.
 * 
 * @author Niklas Schlimm
 * @see DecoratorAwareAutowireCandidateResolver, SimpleDelegateResolutionStrategy
 * 
 */
public interface IgnoreDecoratorAutowiringLogic {}
