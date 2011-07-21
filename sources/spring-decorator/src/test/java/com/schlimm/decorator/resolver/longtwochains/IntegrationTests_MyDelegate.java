package com.schlimm.decorator.resolver.longtwochains;

import javax.decorator.Decorator;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.schlimm.decorator.resolver.longsinglechain.MyDelegate;
import com.schlimm.decorator.resolver.longsinglechain.MyServiceInterface;

@ContextConfiguration("/test-context-decorator-resolver-long-two-chains-integration.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class IntegrationTests_MyDelegate {

	@Autowired
	private MyServiceInterface decoratedInterface;

	@Test
	public void testHelloWorld() {
		Assert.assertTrue(decoratedInterface.sayHello().equals("Hello"));
	}

	@Test
	public void testInjectedObjectIsDecorator() {
		Assert.assertTrue(AopUtils.getTargetClass(decoratedInterface).getAnnotation(Decorator.class) != null);
	}

	@Test
	public void testSecondDecorator() {
		Assert.assertTrue(decoratedInterface.getDelegateObject().getClass().getAnnotation(Decorator.class) != null);
	}

	@Test
	public void testThirdDecorator() {
		Assert.assertTrue(decoratedInterface.getDelegateObject().getDelegateObject().getClass().getAnnotation(Decorator.class) != null);
	}

	@Test
	public void testDelegateTargetObject() {
		Assert.assertTrue(MyDelegate.class.isAssignableFrom(decoratedInterface.getDelegateObject().getDelegateObject().getDelegateObject().getClass()));
	}

	@Test
	public void testDelegateTargetObject_mustBeProxy() {
		Assert.assertTrue(AopUtils.isAopProxy(decoratedInterface.getDelegateObject().getDelegateObject().getDelegateObject()));
	}
}
