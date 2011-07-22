package com.schlimm.springcdi.model;

import java.lang.reflect.Field;

import org.springframework.beans.factory.config.DependencyDescriptor;

public class DelegateField {

	private Field delegateField;

	private DependencyDescriptor dependencyDescriptor;

	public Field getDeclaredField() {
		return delegateField;
	}

	public DelegateField(Field delegateField, DependencyDescriptor dependencyDescriptor) {
		super();
		this.delegateField = delegateField;
		this.dependencyDescriptor = dependencyDescriptor;
	}

	public void setDelegateField(Field delegateField) {
		this.delegateField = delegateField;
	}

	public DependencyDescriptor getDependencyDescriptor() {
		return dependencyDescriptor;
	}

	public void setDependencyDescriptor(DependencyDescriptor dependencyDescriptor) {
		this.dependencyDescriptor = dependencyDescriptor;
	}

	public String toString() {
		return "Field name: " + delegateField.getName() + ", Field type: " + delegateField.getType();
	}

}
