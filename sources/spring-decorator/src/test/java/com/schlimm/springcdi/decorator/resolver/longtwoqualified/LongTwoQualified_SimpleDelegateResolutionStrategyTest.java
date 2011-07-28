package com.schlimm.springcdi.decorator.resolver.longtwoqualified;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.schlimm.springcdi.decorator.model.DecoratorInfo;
import com.schlimm.springcdi.decorator.strategies.DelegateResolutionStrategy;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDelegateResolutionStrategy;


@ContextConfiguration("/test-context-decorator-resolver-long-two-qualified-chains.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class LongTwoQualified_SimpleDelegateResolutionStrategyTest {

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	private DelegateResolutionStrategy delegateResolutionStrategy;
	
	@Before
	public void setUp() {
		delegateResolutionStrategy = new SimpleDelegateResolutionStrategy();
	}
	
	@Test
	public void testChaining_MyDecoratorLeadsToMyDelegate() {
		DecoratorInfo decoratorInfo = null;
		try {
			decoratorInfo = new DecoratorInfo("longTwoQualified_MyDecorator", beanFactory.getBeanDefinition("longTwoQualified_MyDecorator"), Class.forName("com.schlimm.springcdi.decorator.resolver.longtwoqualified.LongTwoQualified_MyDecorator"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("longTwoQualified_MyDelegate"));
	}

	@Test
	public void testChaining_MyDecorator2LeadsToMyDelegate() {
		DecoratorInfo decoratorInfo = null;
		try {
			decoratorInfo = new DecoratorInfo("longTwoQualified_MyDecorator2", beanFactory.getBeanDefinition("longTwoQualified_MyDecorator2"), Class.forName("com.schlimm.springcdi.decorator.resolver.longtwoqualified.LongTwoQualified_MyDecorator2"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("longTwoQualified_MyDelegate"));
	}
	
	@Test
	public void testChaining_AnotherDecoratorLeadsToAnotherDelegate() {
		DecoratorInfo decoratorInfo = null;
		try {
			decoratorInfo = new DecoratorInfo("longTwoQualified_AnotherDecorator", beanFactory.getBeanDefinition("longTwoQualified_AnotherDecorator"), Class.forName("com.schlimm.springcdi.decorator.resolver.longtwoqualified.LongTwoQualified_AnotherDecorator"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("longTwoQualified_AnotherDelegate"));
	}
	
	@Test
	public void testChaining_AnotherDecorator2LeadsToAnotherDelegate() {
		DecoratorInfo decoratorInfo = null;
		try {
			decoratorInfo = new DecoratorInfo("longTwoQualified_AnotherDecorator2", beanFactory.getBeanDefinition("longTwoQualified_AnotherDecorator2"), Class.forName("com.schlimm.springcdi.decorator.resolver.longtwoqualified.LongTwoQualified_AnotherDecorator2"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("longTwoQualified_AnotherDelegate"));
	}
	
}
