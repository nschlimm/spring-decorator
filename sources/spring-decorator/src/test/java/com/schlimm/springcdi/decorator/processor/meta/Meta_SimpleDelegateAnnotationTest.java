package com.schlimm.springcdi.decorator.processor.meta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.schlimm.springcdi.decorator.processor.DelegateProxyInspector;



@ContextConfiguration("/test-context-decorator-processor-meta.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class Meta_SimpleDelegateAnnotationTest {

	// must be delegate
	@Autowired 
	private Meta_MyDelegate delegate;
	
	// must be decorator
	@Autowired 
	private Meta_MyServiceInterface decoratedInterface;
	
	@Test
	public void testHelloWorld(){
		Assert.isTrue(decoratedInterface.sayHello().equals("Hello"));
		Assert.isTrue(delegate.sayHello().equals("Hello"));
	}
	
	@Test
	public void testDelegateClassInjection() {
		Assert.isTrue(Meta_MyDelegate.class.isAssignableFrom(delegate.getClass()));
	}
	
	@Test
	public void testServiceInterfaceInjection() {
		Assert.isTrue(AopUtils.isAopProxy(decoratedInterface));
		Assert.isTrue(Meta_MyDelegate.class.isAssignableFrom(AopUtils.getTargetClass(decoratedInterface)));
	}
	
	@Test
	public void testFirstDecorator() {
		DelegateProxyInspector pi = (DelegateProxyInspector)decoratedInterface;
		Assert.isTrue(pi.getInterceptorTarget().getClass().isAssignableFrom(Meta_MyDecorator.class));
	}
	
	@Test
	public void testDelegate() {
		Assert.isTrue(Meta_MyDelegate.class.isAssignableFrom(decoratedInterface.getDelegateObject().getClass()));
	}
	
}
