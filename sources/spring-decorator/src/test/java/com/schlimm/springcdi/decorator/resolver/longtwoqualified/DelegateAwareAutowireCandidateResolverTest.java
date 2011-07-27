package com.schlimm.springcdi.decorator.resolver.longtwoqualified;

import java.lang.reflect.Field;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.schlimm.springcdi.decorator.DecoratorAwareBeanFactoryPostProcessor;
import com.schlimm.springcdi.decorator.model.QualifiedDecoratorChain;
import com.schlimm.springcdi.decorator.resolver.DecoratorAwareAutowireCandidateResolver;
import com.schlimm.springcdi.decorator.resolver.rules.ResolverCDIAutowiringRules;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDecoratorOrderingStrategy;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDecoratorResolutionStrategy;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDelegateResolutionStrategy;

@ContextConfiguration("/test-context-decorator-resolver-long-two-qualified-chains.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class DelegateAwareAutowireCandidateResolverTest {

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	private DecoratorAwareBeanFactoryPostProcessor beanPostProcessor;
	
	private DecoratorAwareAutowireCandidateResolver resolver;

	private SomeTestBean someTestBean = new SomeTestBean();

	private QualifiedDecoratorChain anotherChain;
	
	private Field autowiredInjectionPoint;
	
	private DependencyDescriptor autowiredInjectionPointDependencyDescriptor;
	
	private BeanDefinitionHolder firstDecoratorBeanDef;

	@Before
	public void setUp() {
		beanPostProcessor = new DecoratorAwareBeanFactoryPostProcessor(new SimpleDecoratorResolutionStrategy(), new SimpleDelegateResolutionStrategy(), null, new SimpleDecoratorOrderingStrategy());
		beanPostProcessor.setMode("resolver");
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		resolver = (DecoratorAwareAutowireCandidateResolver) ((DefaultListableBeanFactory) beanFactory).getAutowireCandidateResolver();
		List<QualifiedDecoratorChain> chains = ((ResolverCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains();
		// QualifiedDecoratorChain chainMy= chains.get(0).getDelegateBeanDefinitionHolder().getBeanName().equals("myDelegate") ?
		// chains.get(0) : chains.get(1);
		try {
			autowiredInjectionPoint = someTestBean.getClass().getDeclaredField("decoratedInterface");
		} catch (SecurityException e) {
			TestCase.fail(e.getMessage());
		} catch (NoSuchFieldException e) {
			TestCase.fail(e.getMessage());
		}
		anotherChain = chains.get(1).getDelegateBeanDefinitionHolder().getBeanName().equals("anotherDelegate") ? chains.get(1) : chains.get(0);
		firstDecoratorBeanDef = anotherChain.getDecorators().get(0).getDecoratorBeanDefinitionHolder();
		autowiredInjectionPointDependencyDescriptor = new DependencyDescriptor(autowiredInjectionPoint, true);
	}

	@Test
	public void testAutowiringCandidateResolving_AnotherDelegateIsCandidateForLastAnotherDecorator() {
		Assert.isTrue(resolver.isAutowireCandidate(firstDecoratorBeanDef, autowiredInjectionPointDependencyDescriptor));
	}

}
