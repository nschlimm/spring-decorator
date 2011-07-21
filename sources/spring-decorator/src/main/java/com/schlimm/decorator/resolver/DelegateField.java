package com.schlimm.decorator.resolver;

import java.lang.reflect.Field;

public class DelegateField {

	private Field delegateField;

	private DelegateDependencyDescriptor dependencyDescriptor;

	public Field getDeclaredField() {
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

	public String toString() {
		return "Field name: " + delegateField.getName() + ", Field type: " + delegateField.getType();
	}

}
