package com.schlimm.springcdi.decorator.resolver.aop;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.schlimm.springcdi.decorator.resolver.longtwoqualified.DelegateAwareAutowireCandidateResolverTest;

@ContextConfiguration(inheritLocations=true, locations="/test-context-decorator-resolver-aop.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class AOPEnabled_DelegateAwareAutowireCandidateResolverTest extends DelegateAwareAutowireCandidateResolverTest{

}
