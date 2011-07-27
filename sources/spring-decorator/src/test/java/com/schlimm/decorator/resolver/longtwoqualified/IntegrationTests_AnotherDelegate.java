package com.schlimm.decorator.resolver.longtwoqualified;

import javax.decorator.Decorator;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.schlimm.decorator.resolver.longsinglechain.MyServiceInterface;

@ContextConfiguration("/test-context-decorator-resolver-long-two-qualified-chains-integration.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class IntegrationTests_AnotherDelegate {

	@Autowired @Qualifier("another")
	private MyServiceInterface decoratedInterface;

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
		Assert.assertTrue(AopUtils.getTargetClass(decoratedInterface.getDelegateObject()).getSimpleName().startsWith("AnotherDecorator"));
	}

	@Test
	public void testDelegateTargetObject() {
		Assert.assertTrue(AnotherDelegate.class.isAssignableFrom(decoratedInterface.getDelegateObject().getDelegateObject().getClass()));
	}

	@Test
	public void testDelegateTargetObject_mustBeProxy() {
		Assert.assertTrue(AopUtils.isAopProxy(decoratedInterface.getDelegateObject().getDelegateObject()));
	}
}
