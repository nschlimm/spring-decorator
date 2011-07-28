package com.schlimm.springcdi.decorator.processor.aop;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.schlimm.springcdi.decorator.processor.integration.IntegrationTests_LongTwoChains_MyDelegate;
import com.schlimm.springcdi.decorator.resolver.longsinglechain.MyDecorator;
import com.schlimm.springcdi.decorator.resolver.longsinglechain.MyDelegate;

@ContextConfiguration(inheritLocations=false, locations={"/test-context-decorator-processor-aop-cg.xml", "/test-context-decorator-processor-long-two-chains-integration.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class AOP_CGLIB_Enabled_IntegrationTests_LongTwoChains_MyDelegate extends IntegrationTests_LongTwoChains_MyDelegate {
	
	@Test
	public void testInjectedObject() {
		Assert.assertTrue(MyDelegate.class.isAssignableFrom(AopUtils.getTargetClass(decoratedInterface)));
	}

	@Test
	public void testProxyType() {
		Object decorator1 = decoratedInterface.getDelegateObject();
		Object decorator2 = decoratedInterface.getDelegateObject().getDelegateObject();
		Object decorator3 = decoratedInterface.getDelegateObject().getDelegateObject().getDelegateObject();
		if (MyDecorator.class.isAssignableFrom(decorator1.getClass())) {
			Assert.assertTrue(AopUtils.isCglibProxy(decorator1)); return;
		}
		if (MyDecorator.class.isAssignableFrom(decorator2.getClass())) {
			Assert.assertTrue(AopUtils.isCglibProxy(decorator2)); return;
		}
		if (MyDecorator.class.isAssignableFrom(decorator3.getClass())) {
			Assert.assertTrue(AopUtils.isCglibProxy(decorator2)); return;
		}
		TestCase.fail();
	}

}
