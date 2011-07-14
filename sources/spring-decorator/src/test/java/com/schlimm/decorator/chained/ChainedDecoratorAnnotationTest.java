package com.schlimm.decorator.chained;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.schlimm.decorator.simple.MyDecorator;
import com.schlimm.decorator.simple.MyDelegate;
import com.schlimm.decorator.simple.MyServiceInterface;

@ContextConfiguration("/test-context-decorator-chained.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ChainedDecoratorAnnotationTest {

	// must be decorator
	@Autowired
	private MyDecorator decorator;
	
	// Must be decorator
	@Autowired
	private MyDecorator2 decorator2;

	// must be delegate
	@Autowired 
	private MyDelegate delegate;
	
	// must be decorator
	@Autowired 
	private MyServiceInterface decoratedInterface;
	
	/**
	 * If MyDecorator is field type is autowired then MyDecorator gets injected
	 */
	@Test
	public void testMyDecoratorFieldTypeAutowiring() {
		Assert.isTrue(decorator.getClass().equals(MyDecorator.class));
	}
	
	/**
	 * If MyDelegate is field type is autowired then MyDelegate gets injected
	 */
	@Test
	public void testMyDelegateFieldTypeAutowiring() {
		Assert.isTrue(delegate.getClass().equals(MyDelegate.class));
	}
	
	/**
	 * If MyDecorator2 is field type is autowired then MyDecorator2 gets injected
	 */
	@Test
	public void testMyDecorator2FieldTypeAutowiring() {
		Assert.isTrue(decorator2.getClass().equals(MyDecorator2.class));
	}
	
	/**
	 * If the MyServiceInterface is field type then the MyDecorator2 instance should be injected
	 */
	@Test
	public void testMyServiceInterfaceFieldTypeAutowiring() {
		Assert.isTrue(decoratedInterface.getClass().equals(MyDecorator2.class));
	}
	
	/**
	 * The delegate of MyDecorator2 instance should be of type MyDecorator
	 */
	@Test
	public void testMyDecorator2ShouldAutowireMyDecorator() {
		Assert.isTrue(decoratedInterface.getDelegateInterface().getClass().equals(MyDecorator.class));
	}
	
	/**
	 * The delegate of MyDecorator instance should be of type MyServiceInterface
	 */
	@Test
	public void testMyDecoratorShouldAutowireMyServiceInterface() {
		Assert.isTrue(decoratedInterface.getDelegateInterface().getDelegateInterface().getClass().equals(MyDelegate.class));
	}
	
//	/**
//	 * If you inject the delegate in the decorator by interface type, then the delegate class instance is injected
//	 */
//	@Test
//	public void testDelegateAutowiringInDecorator_DelegateInterface() {
//		Assert.isTrue(decorator.getDelegateInterface().getClass().equals(MyDelegate.class));
//	}
//	
//	/**
//	 * If you inject the delegate somewhere by class, then the delegate class instance is injected
//	 */
//	@Test
//	public void testDecoratorClassInjection() {
//		Assert.isTrue(delegate.getClass().equals(MyDelegate.class));
//	}
//	
//	/**
//	 * If you inject the decorated interface somewhere by interface type, then the decorator class instance is injected
//	 * If you inject the MyServiceInterface interface into the decorator, then the delegate instance gets injected
//	 * If you inject the MyDelegate class into the decorator, then the delegate instance gets injected
//	 */
//	@Test
//	public void testDecoratorInterfaceInjection() {
//		Assert.isTrue(decoratedInterface.getClass().equals(MyDecorator.class));
//		Assert.isTrue(decoratedInterface.getDelegateClass().getClass().equals(MyDelegate.class));
//		Assert.isTrue(decoratedInterface.getDelegateInterface().getClass().equals(MyDelegate.class));
//	}
//	
//	/**
//	 * If you inject the Decorator2 class somewhere it must be the Decorator2 instance
//	 */
//	@Test
//	public void testDecorator2Injection() {
//		Assert.isTrue(decorator2.getClass().equals(MyDecorator2.class));
//	}
}
