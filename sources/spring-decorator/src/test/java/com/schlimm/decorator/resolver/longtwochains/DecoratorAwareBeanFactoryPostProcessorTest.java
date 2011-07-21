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

import com.schlimm.decorator.DecoratorAwareBeanFactoryPostProcessor;
import com.schlimm.decorator.SimpleDecoratorResolutionStrategy;
import com.schlimm.decorator.SimpleDelegateResolutionStrategy;
import com.schlimm.decorator.resolver.DelegateAwareAutowireCandidateResolver;



@ContextConfiguration("/test-context-decorator-resolver-long-two-chains.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DecoratorAwareBeanFactoryPostProcessorTest {

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	private DecoratorAwareBeanFactoryPostProcessor beanPostProcessor;
	
	@Before
	public void setUp() {
		beanPostProcessor = new DecoratorAwareBeanFactoryPostProcessor(new SimpleDecoratorResolutionStrategy(), new SimpleDelegateResolutionStrategy());
	}
	
	@Test
	public void testChaining_MustBeTwoChains() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DelegateAwareAutowireCandidateResolver resolver = (DelegateAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(resolver.getDecoratorChains().size()==2);
	}

	@Test
	public void testChaining_MyDelegateMustBeDelegate() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DelegateAwareAutowireCandidateResolver resolver = (DelegateAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(resolver.getDecoratorChains().get(0).getDelegateBeanDefinitionHolder().getBeanName().equals("myDelegate"));
	}
	
	@Test
	public void testChaining_AnotherDelegateMustBeDelegate() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DelegateAwareAutowireCandidateResolver resolver = (DelegateAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(resolver.getDecoratorChains().get(1).getDelegateBeanDefinitionHolder().getBeanName().equals("anotherDelegate"));
	}
	
	@Test
	public void testChaining_MustBeThreeDecoratorsInBothChains() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DelegateAwareAutowireCandidateResolver resolver = (DelegateAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(resolver.getDecoratorChains().get(0).getDecorators().size()==3);
		Assert.isTrue(resolver.getDecoratorChains().get(1).getDecorators().size()==3);
	}
	
}
