package com.schlimm.decorator.resolver;

import java.lang.reflect.Field;

import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.core.MethodParameter;

public class DelegateDependencyDescriptor extends DependencyDescriptor {

	/**
	 * Unique ID
	 */
	private static final long serialVersionUID = 60456315558049329L;

	public DelegateDependencyDescriptor(Field field, boolean required, boolean eager) {
		super(field, required, eager);
	}

	public DelegateDependencyDescriptor(Field field, boolean required) {
		super(field, required);
	}

	public DelegateDependencyDescriptor(MethodParameter methodParameter, boolean required, boolean eager) {
		super(methodParameter, required, eager);
	}

	public DelegateDependencyDescriptor(MethodParameter methodParameter, boolean required) {
		super(methodParameter, required);
	}

	public boolean equals(DependencyDescriptor dependencyDescriptor) {
		return (this.getField().equals(dependencyDescriptor.getField()));
	}

}
