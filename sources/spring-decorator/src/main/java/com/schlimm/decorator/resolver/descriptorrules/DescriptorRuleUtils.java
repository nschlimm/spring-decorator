package com.schlimm.decorator.resolver.descriptorrules;

import java.lang.reflect.Field;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import org.springframework.beans.factory.config.DependencyDescriptor;

public class DescriptorRuleUtils {

	@SuppressWarnings("rawtypes")
	public static DependencyDescriptor createRuleBasedDescriptor(Field field, Class[] ruleTags) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(DependencyDescriptor.class);
		enhancer.setCallback(NoOp.INSTANCE);
		enhancer.setInterfaces(ruleTags);
		DependencyDescriptor desc = (DependencyDescriptor) enhancer.create(new Class[] { Field.class, boolean.class }, new Object[] { field, true });
		return desc;
	}


}
