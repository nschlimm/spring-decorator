package com.schlimm.springcdi.decorator.resolver.simple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;



@ContextConfiguration("/test-context-decorator-resolver-simple.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class Simple_SimpleDelegateAnnotationTest {

	// must be decorator
	@Autowired
	private Simple_MyDecorator decorator;

	// must be delegate
	@Autowired 
	private Simple_MyDelegate delegate;
	
	// must be decorator
	@Autowired 
	private Simple_MyServiceInterface decoratedInterface;
	
	/**
	 * If you inject the delegate in the decorator by interface type, then the delegate class instance is injected
	 */
	@Test
	public void testDelegateAutowiringInDecorator_DelegateInterface() {
		Assert.isTrue(Simple_MyDelegate.class.isAssignableFrom(decorator.getDelegateInterface().getClass()));
	}
	
	/**
	 * If you inject the delegate somewhere by class, then the delegate class instance is injected
	 */
	@Test
	public void testDecoratorClassInjection() {
		Assert.isTrue(Simple_MyDelegate.class.isAssignableFrom(delegate.getClass()));
	}
	
	/**
	 * If you inject the decorated interface somewhere by interface type, then the decorator class instance is injected
	 * If you inject the LongSingleChain_MyServiceInterface interface into the decorator, then the delegate instance gets injected
	 * If you inject the LongSingleChain_MyDelegate class into the decorator, then the delegate instance gets injected
	 */
	@Test
	public void testDecoratorInterfaceInjection() {
		Assert.isTrue(decoratedInterface.getClass().equals(Simple_MyDecorator.class));
		Assert.isTrue(Simple_MyDelegate.class.isAssignableFrom(decoratedInterface.getDelegateInterface().getClass()));
	}
	
}
