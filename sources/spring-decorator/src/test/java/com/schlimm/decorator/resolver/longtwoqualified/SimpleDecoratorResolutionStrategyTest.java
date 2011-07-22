package com.schlimm.decorator.resolver.longtwoqualified;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.schlimm.decorator.DecoratorResolutionStrategy;
import com.schlimm.decorator.SimpleDecoratorResolutionStrategy;


@ContextConfiguration("/test-context-decorator-resolver-long-two-qualified-chains.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleDecoratorResolutionStrategyTest {

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	private DecoratorResolutionStrategy decoratorResolutionStrategy;
	
	@Before
	public void setUp() {
		decoratorResolutionStrategy = new SimpleDecoratorResolutionStrategy();
	}
	
	@Test
	public void testChaining_MustBeSixDecorators() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).size()==4);
	}

	@Test
	public void testChaining_ContainsMyDecorator() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("myDecorator"));
	}
	
	@Test
	public void testChaining_ContainsMyDecorator2() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("myDecorator2"));
	}
	
	@Test
	public void testChaining_ContainsAnotherDecorator() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("anotherDecorator"));
	}
	
	@Test
	public void testChaining_ContainsAnotherDecorator2() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("anotherDecorator2"));
	}
}
