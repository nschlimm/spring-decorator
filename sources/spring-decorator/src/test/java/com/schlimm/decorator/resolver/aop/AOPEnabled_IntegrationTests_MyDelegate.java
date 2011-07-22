package com.schlimm.decorator.resolver.aop;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.schlimm.decorator.resolver.longtwoqualified.IntegrationTests_MyDelegate;

@ContextConfiguration(inheritLocations=true, locations="/test-context-decorator-resolver-long-two-qualified-chains-aop.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class AOPEnabled_IntegrationTests_MyDelegate extends IntegrationTests_MyDelegate {

}
