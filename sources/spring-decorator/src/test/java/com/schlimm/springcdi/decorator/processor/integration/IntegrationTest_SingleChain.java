package com.schlimm.springcdi.decorator.processor.integration;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.schlimm.springcdi.decorator.resolver.longsinglechain.LongSingleChain_IntegrationTests;
import com.schlimm.springcdi.decorator.resolver.longsinglechain.LongSingleChain_MyDelegate;



@ContextConfiguration(inheritLocations=false, locations="/test-context-decorator-processor-long-single-chain.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class IntegrationTest_SingleChain extends LongSingleChain_IntegrationTests {
	
	@Test
	public void testInjectedObject() {
		Assert.assertTrue(LongSingleChain_MyDelegate.class.isAssignableFrom(AopUtils.getTargetClass(decoratedInterface)));
	}

	
}