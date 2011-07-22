package com.schlimm.decorator.resolver.longtwochains;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.schlimm.springcdi.decorator.DecoratorAwareBeanFactoryPostProcessor;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDecoratorOrderingStrategy;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDecoratorResolutionStrategy;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDelegateResolutionStrategy;
import com.schlimm.springcdi.resolver.DecoratorAwareAutowireCandidateResolver;
import com.schlimm.springcdi.resolver.rules.SimpleCDIAutowiringRules;



@ContextConfiguration("/test-context-decorator-resolver-long-two-chains.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DecoratorAwareBeanFactoryPostProcessorTest {

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	private DecoratorAwareBeanFactoryPostProcessor beanPostProcessor;
	
	@Before
	public void setUp() {
		beanPostProcessor = new DecoratorAwareBeanFactoryPostProcessor(new SimpleDecoratorResolutionStrategy(), new SimpleDelegateResolutionStrategy(), null, new SimpleDecoratorOrderingStrategy());
	}
	
	@Test
	public void testChaining_MustBeTwoChains() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DecoratorAwareAutowireCandidateResolver resolver = (DecoratorAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((SimpleCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().size()==2);
	}

	@Test
	public void testChaining_MyDelegateMustBeDelegate() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DecoratorAwareAutowireCandidateResolver resolver = (DecoratorAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((SimpleCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().get(0).getDelegateBeanDefinitionHolder().getBeanName().equals("myDelegate"));
	}
	
	@Test
	public void testChaining_AnotherDelegateMustBeDelegate() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DecoratorAwareAutowireCandidateResolver resolver = (DecoratorAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((SimpleCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().get(1).getDelegateBeanDefinitionHolder().getBeanName().equals("anotherDelegate"));
	}
	
	@Test
	public void testChaining_MustBeThreeDecoratorsInBothChains() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DecoratorAwareAutowireCandidateResolver resolver = (DecoratorAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((SimpleCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().get(0).getDecorators().size()==3);
		Assert.isTrue(((SimpleCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().get(1).getDecorators().size()==3);
	}
	
}
