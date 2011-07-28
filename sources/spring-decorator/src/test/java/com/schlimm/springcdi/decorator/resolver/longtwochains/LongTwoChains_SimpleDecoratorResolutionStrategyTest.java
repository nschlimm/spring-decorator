package com.schlimm.springcdi.decorator.resolver.longtwochains;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.schlimm.springcdi.decorator.strategies.DecoratorResolutionStrategy;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDecoratorResolutionStrategy;


@ContextConfiguration("/test-context-decorator-resolver-long-two-chains.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class LongTwoChains_SimpleDecoratorResolutionStrategyTest {

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	private DecoratorResolutionStrategy decoratorResolutionStrategy;
	
	@Before
	public void setUp() {
		decoratorResolutionStrategy = new SimpleDecoratorResolutionStrategy();
	}
	
	@Test
	public void testChaining_MustBeSixDecorators() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).size()==6);
	}

	@Test
	public void testChaining_ContainsMyDecorator() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("longSingleChain_MyDecorator"));
	}
	
	@Test
	public void testChaining_ContainsMyDecorator2() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("longSingleChain_MyDecorator2"));
	}
	
	@Test
	public void testChaining_ContainsMyDecorator3() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("longSingleChain_MyDecorator3"));
	}
	
	@Test
	public void testChaining_ContainsAnotherDecorator() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("longTwoChains_AnotherDecorator"));
	}
	
	@Test
	public void testChaining_ContainsAnotherDecorator2() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("longTwoChains_AnotherDecorator2"));
	}
	
	@Test
	public void testChaining_ContainsAnotherDecorator3() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("longTwoChains_AnotherDecorator3"));
	}
	
}
