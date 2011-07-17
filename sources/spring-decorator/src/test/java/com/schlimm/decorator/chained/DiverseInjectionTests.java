package com.schlimm.decorator.chained;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@ContextConfiguration("/test-context-decorator-chained-alternate.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DiverseInjectionTests {

	// must be decorator
	@Autowired 
	private MyDelegate delegate;
	
	@Test
	public void testDirectInjectionDelegate() {
		Assert.isTrue(MyDecorator.class.isAssignableFrom(delegate.getClass()));
	}
	
}
