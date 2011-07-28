package com.schlimm.springcdi.decorator.processor.aop;

import java.lang.reflect.Proxy;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.schlimm.springcdi.decorator.processor.integration.IntegrationTest_SingleChain;
import com.schlimm.springcdi.decorator.resolver.longsinglechain.MyDecorator;



@ContextConfiguration(inheritLocations=false, locations={"/test-context-decorator-processor-aop.xml", "/test-context-decorator-processor-long-single-chain.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class AOP_Enabled_IntegrationTest_SingleChain extends IntegrationTest_SingleChain { 
	
	@Test
	public void testProxyType() {
		Object decorator1 = decoratedInterface.getDelegateObject();
		Object decorator2 = decoratedInterface.getDelegateObject().getDelegateObject();
		Object decorator3 = decoratedInterface.getDelegateObject().getDelegateObject().getDelegateObject();
		if (Proxy.isProxyClass(decorator1.getClass())&&MyDecorator.class.isAssignableFrom(AopUtils.getTargetClass(decorator1))) {
			Assert.assertTrue(AopUtils.isJdkDynamicProxy(decorator1)); return;
		}
		if (Proxy.isProxyClass(decorator2.getClass())&&MyDecorator.class.isAssignableFrom(AopUtils.getTargetClass(decorator2))) {
			Assert.assertTrue(AopUtils.isJdkDynamicProxy(decorator2)); return;
		}
		if (Proxy.isProxyClass(decorator3.getClass())&&MyDecorator.class.isAssignableFrom(AopUtils.getTargetClass(decorator3))) {
			Assert.assertTrue(AopUtils.isJdkDynamicProxy(decorator3)); return;
		}
		TestCase.fail();
	}
	
}