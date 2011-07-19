package com.schlimm.decorator.resolver;

import java.lang.reflect.Field;

public class DelegateField {
	
	private Field delegateField;
	
	private DelegateDependencyDescriptor dependencyDescriptor;

	public Field getDelegateField() {
		return delegateField;
	}

	public DelegateField(Field delegateField, DelegateDependencyDescriptor dependencyDescriptor) {
		super();
		this.delegateField = delegateField;
		this.dependencyDescriptor = dependencyDescriptor;
	}

	public void setDelegateField(Field delegateField) {
		this.delegateField = delegateField;
	}

	public DelegateDependencyDescriptor getDependencyDescriptor() {
		return dependencyDescriptor;
	}

	public void setDependencyDescriptor(DelegateDependencyDescriptor dependencyDescriptor) {
		this.dependencyDescriptor = dependencyDescriptor;
	}

}
