package com.schlimm.springcdi.model;

import java.lang.reflect.Field;

import org.springframework.beans.factory.config.DependencyDescriptor;

/**
 * Wraps the delegate {@link Field} that is declared in a decorator.
 * 
 * @author Niklas Schlimm
 *
 */
public class DelegateField {

	/**
	 * The declared field of the decorator class that has the delegate annotation
	 */
	private Field delegateField;

	/**
	 * The dependency descriptor for the delegate field
	 */
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
