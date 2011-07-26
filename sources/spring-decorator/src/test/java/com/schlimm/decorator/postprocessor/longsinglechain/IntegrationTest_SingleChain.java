package com.schlimm.decorator.postprocessor.longsinglechain;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.schlimm.decorator.resolver.longsinglechain.IntegrationTests;
import com.schlimm.decorator.resolver.longsinglechain.MyDelegate;



@ContextConfiguration(inheritLocations=false, locations="/test-context-decorator-postprocessor-long-single-chain.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class IntegrationTest_SingleChain extends IntegrationTests {
	
	@Test
	public void testInjectedObject() {
		Assert.assertTrue(MyDelegate.class.isAssignableFrom(AopUtils.getTargetClass(decoratedInterface)));
	}

	
}