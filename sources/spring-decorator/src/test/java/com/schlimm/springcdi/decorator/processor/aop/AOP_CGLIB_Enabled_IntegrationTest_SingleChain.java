package com.schlimm.springcdi.decorator.processor.aop;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.schlimm.springcdi.decorator.processor.integration.IntegrationTest_SingleChain;
import com.schlimm.springcdi.decorator.resolver.aop.NotVeryUsefulAspect;
import com.schlimm.springcdi.decorator.resolver.longsinglechain.LongSingleChain_MyDecorator;

@ContextConfiguration(inheritLocations = false, locations = { "/test-context-decorator-processor-aop-cg.xml", "/test-context-decorator-processor-long-single-chain.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class AOP_CGLIB_Enabled_IntegrationTest_SingleChain extends IntegrationTest_SingleChain {

	/**
	 * {@link LongSingleChain_MyDecorator} must be AOP (CGI) proxied with {@link NotVeryUsefulAspect}
	 */
	@Test
	public void testProxyType() {
		Object decorator1 = decoratedInterface.getDelegateObject();
		Object decorator2 = decoratedInterface.getDelegateObject().getDelegateObject();
		Object decorator3 = decoratedInterface.getDelegateObject().getDelegateObject().getDelegateObject();
		if (LongSingleChain_MyDecorator.class.isAssignableFrom(decorator1.getClass())) {
			Assert.assertTrue(checkCGLIBProxy(decorator1));
			return;
		}
		if (LongSingleChain_MyDecorator.class.isAssignableFrom(decorator2.getClass())) {
			Assert.assertTrue(checkCGLIBProxy(decorator2));
			return;
		}
		if (LongSingleChain_MyDecorator.class.isAssignableFrom(decorator3.getClass())) {
			Assert.assertTrue(checkCGLIBProxy(decorator3));
			return;
		}
		TestCase.fail();
	}

	public static boolean checkCGLIBProxy(Object decorator) {
		Advised advised = (Advised) decorator;
		AspectJMethodBeforeAdvice beforeAdvice = (AspectJMethodBeforeAdvice) advised.getAdvisors()[1].getAdvice();
		return AopUtils.isCglibProxy(decorator) && advised.getAdvisors()[1].getAdvice().getClass().equals(AspectJMethodBeforeAdvice.class)
				&& beforeAdvice.getAspectName().startsWith("com.schlimm.springcdi.");
	}

	public static boolean checkJDKProxy(Object decorator) {
		Advised advised = (Advised) decorator;
		AspectJMethodBeforeAdvice beforeAdvice = (AspectJMethodBeforeAdvice) advised.getAdvisors()[1].getAdvice();
		return AopUtils.isJdkDynamicProxy(decorator) && advised.getAdvisors()[1].getAdvice().getClass().equals(AspectJMethodBeforeAdvice.class)
		&& beforeAdvice.getAspectName().startsWith("com.schlimm.springcdi.");
	}
	
}