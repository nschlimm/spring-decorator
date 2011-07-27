package com.schlimm.springcdi.decorator;

import java.lang.reflect.Field;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.config.DependencyDescriptor;

/**
 * Reused methods in this Spring-CDI module.
 * 
 * @author Niklas Schlimm
 *
 */
public class DecoratorModuleUtils {

	@SuppressWarnings("rawtypes")
	public static DependencyDescriptor createRuleBasedDescriptor(Field field, Class[] ruleTags) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(DependencyDescriptor.class);
		enhancer.setCallback(NoOp.INSTANCE);
		enhancer.setInterfaces(ruleTags);
		DependencyDescriptor desc = (DependencyDescriptor) enhancer.create(new Class[] { Field.class, boolean.class }, new Object[] { field, true });
		return desc;
	}

	public static Object locateAopTarget(String beanName, Object targetBean) {
		Advised advised = (Advised) targetBean;
		try {
			targetBean = advised.getTargetSource().getTarget();
			if (AopUtils.isAopProxy(targetBean)){
				// Recursion if more then one AOP proxy applied
				return locateAopTarget(beanName, targetBean);
			}
		} catch (Exception e) {
			throw new DecoratorAwareBeanFactoryPostProcessorException("Could not locate target bean: " + beanName, e);
		}
		return targetBean;
	}
	
}
