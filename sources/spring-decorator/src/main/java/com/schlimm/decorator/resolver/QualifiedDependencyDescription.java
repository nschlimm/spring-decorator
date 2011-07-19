package com.schlimm.decorator.resolver;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.springframework.beans.factory.config.DependencyDescriptor;

@SuppressWarnings("rawtypes")
public class QualifiedDependencyDescription {

	/**
	 * Qualifier annotations at the injection point.
	 */
	private Annotation[] fieldAnnotations;

	/**
	 * Type of the field at the injection point.
	 */
	private Class fieldType;

	public QualifiedDependencyDescription(Annotation[] fieldAnnotations, Class fieldType) {
		super();
		this.fieldAnnotations = fieldAnnotations;
		this.fieldType = fieldType;
	}

	public QualifiedDependencyDescription(DependencyDescriptor descriptor) {
		super();
		this.fieldAnnotations = descriptor.getAnnotations();
		this.fieldType = descriptor.getField().getType();
	}
	
	public void setFieldAnnotations(Annotation[] fieldAnnotations) {
		this.fieldAnnotations = fieldAnnotations;
	}

	public Annotation[] getFieldAnnotations() {
		return fieldAnnotations;
	}

	public void setFieldType(Class fieldType) {
		this.fieldType = fieldType;
	}

	public Class getFieldType() {
		return fieldType;
	}

	public boolean equals(QualifiedDependencyDescription desc) {
		return (Arrays.equals(desc.getFieldAnnotations(), this.fieldAnnotations) && fieldType.equals(desc.getFieldType()));
	}

}
