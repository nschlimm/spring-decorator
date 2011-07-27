package com.schlimm.springcdi.decorator.processor.integration;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.schlimm.springcdi.decorator.resolver.longtwoqualified.IntegrationTests_MyDelegate;
import com.schlimm.springcdi.decorator.resolver.longtwoqualified.MyDelegate;

@ContextConfiguration(inheritLocations=false, locations="/test-context-decorator-processor-long-two-qualified-chains-integration.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class IntegrationTests_LongTwoQualified_MyDelegate extends IntegrationTests_MyDelegate {

	@Test
	public void testInjectedObject() {
		Assert.assertTrue(MyDelegate.class.isAssignableFrom(AopUtils.getTargetClass(decoratedInterface)));
	}
	
}
