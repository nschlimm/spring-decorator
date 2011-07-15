package com.schlimm.decorator.chained2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@ContextConfiguration("/test-context-decorator-chained-alternate.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ChainedDecoratorAnnotationTest {

	// must be decorator
	@Autowired 
	private MyDelegate delegate;
	
	// must be decorator
	@Autowired 
	private MyServiceInterface decoratedInterface;
	
	/**
	 * If MyDelegate is field type is autowired then MyDelegate (or proxy subclass) gets injected
	 */
	@Test
	public void testMyDelegateFieldTypeAutowiring() {
		Assert.isTrue(MyDelegate.class.isAssignableFrom(delegate.getClass()));
	}
	
	/**
	 * If the MyServiceInterface is field type then the MyDecorator2 instance should be injected
	 */
	@Test
	public void testMyServiceInterfaceFieldTypeAutowiring() {
		Assert.isTrue(decoratedInterface.getClass().equals(MyDecorator3.class));
	}
	
	/**
	 * The delegate of MyDecorator2 instance should be of type MyDecorator
	 */
	@Test
	public void testMyDecorator2ShouldAutowireMyDecorator() {
		Assert.isTrue(decoratedInterface.getDelegateInterface().getClass().equals(MyDecorator.class));
	}
	
	/**
	 * The delegate of MyDecorator instance should be of type MyDelegate (or subclass -> proxy)
	 */
	@Test
	public void testMyDecoratorShouldAutowireMyServiceInterface() {
		Assert.isTrue(MyDelegate.class.isAssignableFrom(decoratedInterface.getDelegateInterface().getDelegateInterface().getClass()));
	}
	
}
