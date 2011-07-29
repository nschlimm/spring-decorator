package com.schlimm.springcdi.decorator.processor.aop;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.schlimm.springcdi.decorator.processor.integration.IntegrationTests_LongTwoQualified_MyDelegate;
import com.schlimm.springcdi.decorator.resolver.aop.NotVeryUsefulAspect;
import com.schlimm.springcdi.decorator.resolver.longtwoqualified.LongTwoQualified_MyDecorator;
import com.schlimm.springcdi.decorator.resolver.longtwoqualified.LongTwoQualified_MyDelegate;

/**
 * Test Spring AOP CGLIB Proxies compatibility of Spring-CDI decorator module.
 * 
 * @author Niklas Schlimm
 *
 */
@ContextConfiguration(inheritLocations=false, locations={"/test-context-decorator-processor-aop-cg.xml", "/test-context-decorator-processor-long-two-qualified-chains-integration.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class AOP_CGLIB_Enabled_IntegrationTests_LongTwoQualified_MyDelegate extends IntegrationTests_LongTwoQualified_MyDelegate {

	/**
	 * Injected object must be of type {@link LongTwoQualified_MyDecorator}
	 */
	@Test
	public void testInjectedObject() {
		Assert.assertTrue(LongTwoQualified_MyDelegate.class.isAssignableFrom(AopUtils.getTargetClass(decoratedInterface)));
	}
	
	/**
	 * {@link LongTwoQualified_MyDecorator} must be AOP (CGI) proxied with {@link NotVeryUsefulAspect}
	 */
	@Test
	public void testProxyType() {
		Object decorator1 = decoratedInterface.getDelegateObject();
		Object decorator2 = decoratedInterface.getDelegateObject().getDelegateObject();
		if (LongTwoQualified_MyDecorator.class.isAssignableFrom(decorator1.getClass())) {
			Assert.assertTrue(AOP_CGLIB_Enabled_IntegrationTest_SingleChain.checkCGLIBProxy(decorator1)); return;
		}
		if (LongTwoQualified_MyDecorator.class.isAssignableFrom(decorator2.getClass())) {
			Assert.assertTrue(AOP_CGLIB_Enabled_IntegrationTest_SingleChain.checkCGLIBProxy(decorator2)); return;
		}
		TestCase.fail();
	}

}
