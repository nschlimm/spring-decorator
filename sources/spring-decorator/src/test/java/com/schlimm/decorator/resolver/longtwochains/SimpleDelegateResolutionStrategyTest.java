package com.schlimm.decorator.resolver.longtwochains;

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

import com.schlimm.springcdi.decorator.strategies.DelegateResolutionStrategy;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDelegateResolutionStrategy;
import com.schlimm.springcdi.model.DecoratorInfo;


@ContextConfiguration("/test-context-decorator-resolver-long-two-chains.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class SimpleDelegateResolutionStrategyTest {

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
			decoratorInfo = new DecoratorInfo("myDecorator", beanFactory.getBeanDefinition("myDecorator"), Class.forName("com.schlimm.decorator.resolver.longsinglechain.MyDecorator"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("myDelegate"));
	}

	@Test
	public void testChaining_MyDecorator2LeadsToMyDelegate() {
		DecoratorInfo decoratorInfo = null;
		try {
			decoratorInfo = new DecoratorInfo("myDecorator2", beanFactory.getBeanDefinition("myDecorator2"), Class.forName("com.schlimm.decorator.resolver.longsinglechain.MyDecorator2"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("myDelegate"));
	}
	
	@Test
	public void testChaining_MyDecorator3LeadsToMyDelegate() {
		DecoratorInfo decoratorInfo = null;
		try {
			decoratorInfo = new DecoratorInfo("myDecorator3", beanFactory.getBeanDefinition("myDecorator3"), Class.forName("com.schlimm.decorator.resolver.longsinglechain.MyDecorator3"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("myDelegate"));
	}
	
	@Test
	public void testChaining_AnotherDecoratorLeadsToAnotherDelegate() {
		DecoratorInfo decoratorInfo = null;
		try {
			decoratorInfo = new DecoratorInfo("anotherDecorator", beanFactory.getBeanDefinition("anotherDecorator"), Class.forName("com.schlimm.decorator.resolver.longtwochains.AnotherDecorator"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("anotherDelegate"));
	}
	
	@Test
	public void testChaining_AnotherDecorator2LeadsToAnotherDelegate() {
		DecoratorInfo decoratorInfo = null;
		try {
			decoratorInfo = new DecoratorInfo("anotherDecorator", beanFactory.getBeanDefinition("anotherDecorator"), Class.forName("com.schlimm.decorator.resolver.longtwochains.AnotherDecorator2"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("anotherDelegate"));
	}
	
	@Test
	public void testChaining_AnotherDecorator3LeadsToAnotherDelegate() {
		DecoratorInfo decoratorInfo = null;
		try {
			decoratorInfo = new DecoratorInfo("anotherDecorator", beanFactory.getBeanDefinition("anotherDecorator"), Class.forName("com.schlimm.decorator.resolver.longtwochains.AnotherDecorator3"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("anotherDelegate"));
	}
	

}
