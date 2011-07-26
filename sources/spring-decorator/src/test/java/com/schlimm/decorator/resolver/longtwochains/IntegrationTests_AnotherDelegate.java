package com.schlimm.decorator.resolver.longtwochains;

import javax.decorator.Decorator;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("/test-context-decorator-resolver-long-two-chains-integration.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class IntegrationTests_AnotherDelegate {

	@Autowired
	private AnotherServiceInterface decoratedInterface;

	@Test
	public void testHelloWorld() {
		Assert.assertTrue(decoratedInterface.sayHello().equals("Hello"));
	}

	@Test
	public void testInjectedObjectIsDecorator() {
		Assert.assertTrue(AopUtils.getTargetClass(decoratedInterface).getAnnotation(Decorator.class) != null);
		Assert.assertTrue(decoratedInterface.getClass().getSimpleName().startsWith("AnotherDecorator"));
	}

	@Test
	public void testSecondDecorator() {
		Assert.assertTrue(AopUtils.getTargetClass(decoratedInterface.getDelegateObject()).getAnnotation(Decorator.class)!= null);
		Assert.assertTrue(decoratedInterface.getDelegateObject().getClass().getSimpleName().startsWith("AnotherDecorator"));
	}

	@Test
	public void testThirdDecorator() {
		Assert.assertTrue(AopUtils.getTargetClass(decoratedInterface.getDelegateObject().getDelegateObject()).getAnnotation(Decorator.class)!= null);
		Assert.assertTrue(decoratedInterface.getDelegateObject().getDelegateObject().getClass().getSimpleName().startsWith("AnotherDecorator"));
	}

	@Test
	public void testDelegateTargetObject() {
		Assert.assertTrue(AnotherDelegate.class.isAssignableFrom(decoratedInterface.getDelegateObject().getDelegateObject().getDelegateObject().getClass()));
	}

	@Test
	public void testDelegateTargetObject_mustBeProxy() {
		Assert.assertTrue(AopUtils.isAopProxy(decoratedInterface.getDelegateObject().getDelegateObject().getDelegateObject()));
	}
}
