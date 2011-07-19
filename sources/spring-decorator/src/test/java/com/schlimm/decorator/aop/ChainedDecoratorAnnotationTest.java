package com.schlimm.decorator.aop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@ContextConfiguration("/test-context-decorator-aop.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ChainedDecoratorAnnotationTest {

	// must be decorator
	@Autowired 
	private MyServiceInterface decoratedInterface;
	
	/**
	 * Chained call functioning
	 */
	@Test
	public void testChainedDecoratorsHelloWorld() {
		Assert.isTrue(decoratedInterface.getDelegateHello().equals("Hello!"));
	}
	
//	/**
//	 * If the MyServiceInterface is field type then the (non-proxied) MyDecorator instance should be injected
//	 */
//	@Test
//	public void testMyServiceInterfaceFieldTypeAutowiring() {
//		Assert.isTrue(decoratedInterface.getClass().equals(MyDecorator.class));
//	}
//	
//	/**
//	 * The delegate object of MyDecorator instance should be of (proxied) type MyDecorator3
//	 */
//	@Test
//	public void testMyServiceInterfaceShouldAutowireMyDecorator() {
//		Assert.isTrue(MyDecorator3.class.isAssignableFrom(decoratedInterface.getDelegateObject().getClass()));
//	}
//	
//	/**
//	 * The delegate object of MyDecorator3 should be proxied MyDelegate
//	 */
//	@Test
//	public void testMyDecoratorShouldAutowireMyDecorator3() {
//		Assert.isTrue(MyDelegate.class.isAssignableFrom(decoratedInterface.getDelegateObject().getDelegateObject().getClass()));
//	}
//	
//	/**
//	 * MyDecorator3 should be proxied
//	 */
//	@Test
//	public void testMyDecorator3ShouldBeProxied() {
//		Assert.isTrue(AopUtils.isCglibProxyClass(decoratedInterface.getDelegateObject().getClass()));
//	}
//	
//	/**
//	 * MyDelegate should be proxied
//	 */
//	@Test
//	public void testMyDelegateShouldBeProxied() {
//		Assert.isTrue(AopUtils.isCglibProxyClass(decoratedInterface.getDelegateObject().getDelegateObject().getClass()));
//	}
	
}
